/* eslint-disable @typescript-eslint/ban-types */
import { EmailInput } from "@components/EmailInput";
import HeaderNavigation from "@components/HeaderNavigation";
import { MeetingData, MeetingsList } from "@components/MeetingsList";
import { CircularProgress } from "@mui/material";
import { NavigatorProps } from "App";
import React from "react";
import { Col, Container, Row } from "react-bootstrap";

export interface OwnerPageState {
	list: MeetingResponse[],
	loaded: 'complete' | 'failed' | 'process' | 'empty',
	email?: string,
}
export interface OwnerPageProps extends NavigatorProps {
}
export type NotificationsResponse = {
    email: string;
    status: string;
    uuid: string;
}
export type MeetingResponse = {
    name: string,
    description: string,
    uuid: string,
    status: string,
    place: string,
    meetingTime: string,
    ownerEmail: string,
    notifications: NotificationsResponse[]
}
class OwnerPage extends React.Component<OwnerPageProps, OwnerPageState> {
    private ownerEmail: string | null = null;
    public constructor(props: OwnerPageProps) {
        super(props);
        this.state = { list: [], loaded: 'empty' };
    }
    private getMeetingsList = async function(this: OwnerPage, email: string)
		: Promise<MeetingResponse[]> {
			const url = `http://localhost:8080/api/meeting/getByOwner?email=${email}`;
			const result = await fetch(url, {method: 'GET'});
			if(!result.ok) {
				throw new Error('Данные не получены')
			}
			return await result.json();
	}
    public override componentDidMount(): void {
		if((this.ownerEmail = window.sessionStorage.getItem('ownerEmail')) != null) {
			this.setState({ email: this.ownerEmail, loaded: 'process' })
			this.getMeetingsList(this.ownerEmail)
				.then(value => {
					this.setState({ 
						loaded: value.length <= 0 ? 'empty' : 'complete', 
						list: value
					})
				})
				.catch(() => this.setState({ loaded: 'failed' }))
		}
	}
    private onEmailChanged = (value: string): void => {
		if(value.length <= 5) return;
		this.setState({loaded: 'process'})
		window.sessionStorage.setItem('ownerEmail', this.ownerEmail = value);
		this.getMeetingsList(value)
			.then(value => {
				this.setState({ 
					loaded: value.length <= 0 ? 'empty' : 'complete', 
					list: value
				})
			})
			.catch(() => this.setState({ loaded: 'failed' }))
	}
	private onSelectionItem = (index: number): void => {
		const { uuid } = this.state.list[index];
        // this.props.navigator('/notification', { state: { uuid: uuid } });
	}
    public override render(): React.ReactNode {
        type MainContentProps = Pick<OwnerPageState, 'list' | 'loaded'>; 
		const MainContent: React.FC<MainContentProps> = (props: MainContentProps) => {
			const { list, loaded } = props;
			const createNoDataContent = (content: React.JSX.Element): React.JSX.Element => {
				return (
					<Container fluid="md">
						<Row className="justify-content-center">
							<Col md={10} sm={12} lg={8}>
							<div style={{
								display: 'flex',
								padding: '40px 20px',
								justifyContent: 'center',
								alignItems: 'center',
								minHeight: '200px'
							}}>{content}</div>
							</Col>
						</Row>
					</Container>
				);
			}
			switch (loaded) {
				case 'complete':
					return (
					<MeetingsList selectionCallback={this.onSelectionItem}
						list={list.map<MeetingData>(({name, status, notifications}) => {
							return { 
								status: ((status: string) => {
									switch(status) {
										case 'NEWER': return 'Новое';
										case 'UPDATED': return 'Обновлен';
										case 'CANCELED': return 'Отменен'
										default: throw 'Нельзя конвертировать'
									}
								})(status),
								name:name,
                                count: notifications.filter(item => item.status == 'CHECKED').length
							}
						})} />
					)
				case 'failed':
					return createNoDataContent((
						<div style={{alignSelf: 'center', justifyItems: 'center', textAlign: 'center'}}>
							<h3 style={{color: 'crimson'}}>Не удалось загрузить мероприятия</h3>
							<p>Попробуйте еще раз чуть позже</p>
						</div>
					))
				case 'empty':
					return createNoDataContent((
						<div style={{alignSelf: 'center', justifyItems: 'center', textAlign: 'center'}}>
							<h3 style={{color: '#333'}}>Список мероприятий пуст</h3>
							<p>Попробуйте еще раз чуть позже</p>
						</div>
					))
				case 'process': return createNoDataContent((<CircularProgress/>))
			}
		} 
		const { list, loaded, email } = this.state;
		return (
			<div style={{padding: '80px 0px'}}>
				<HeaderNavigation/>
				<h1 style={{textAlign: 'center', margin: '0px 0px 20px'}} className='fs-3 fw-medium'>
					Список мероприятий
				</h1>
				<EmailInput changeCallback={(value) => this.onEmailChanged(value)} value={email ?? ''}/>
				<MainContent list={list} loaded={loaded}/>
			</div>
		);
    }
}
export default OwnerPage;