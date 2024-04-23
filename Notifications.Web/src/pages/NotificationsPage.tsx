/* eslint-disable @typescript-eslint/ban-types */
import React from 'react';
import { NotificationData, NotificationsList } from '@components/NotificationsList';
import { EmailInput } from '@components/EmailInput';
import { AlertsInfo } from '@components/AlertsInfo';
import { NavigatorProps } from 'App';
import { CircularProgress } from '@mui/material';
import { Col, Container, Row } from 'react-bootstrap';

export interface NotificationsPageState {
	list: NotificationsResponse[],
	error: boolean,
	loaded: 'complete' | 'failed' | 'process' | 'empty',
	email?: string,
}
export interface NotificationsPageProps extends NavigatorProps {
}
export type NotificationsResponse = {
	meetingTime: string,
	message: string,
	auditoryId: number,
	notificationId: string
}
class NotificationsPage extends React.Component<NotificationsPageProps, NotificationsPageState> {
	private currentEmail: string | null = null;
    public constructor(props: NotificationsPageProps) {
		super(props);
		this.state = { list: [], error: false, loaded: 'empty' };
	}
	private getNotificationsList = async function(this: NotificationsPage, email: string)
		: Promise<NotificationsResponse[]> {
			const url = `http://localhost:8080/api/getByStatus?status=CHECKING&email=${email}`;
			const result = await fetch(url, {method: 'GET'});
			if(!result.ok) {
				throw new Error('Данные не получены')
			}
			return await result.json();
	}
	public override componentDidMount(): void {
		if((this.currentEmail = window.sessionStorage.getItem('currentEmail')) != null) {
			this.setState({ email: this.currentEmail, loaded: 'process' })
			this.getNotificationsList(this.currentEmail)
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
		window.sessionStorage.setItem('currentEmail', this.currentEmail = value);
		this.getNotificationsList(value)
			.then(value => {
				this.setState({ 
					loaded: value.length <= 0 ? 'empty' : 'complete', 
					list: value
				})
			})
			.catch(() => this.setState({ loaded: 'failed' }))
	}
	private onSelectionItem = (index: number): void => {
        this.props.navigator('/notification', { 
			state: { uuid: this.state.list[index].notificationId } 
		});
	}
	public override render(): React.ReactNode {
		type MainContentProps = Pick<NotificationsPageState, 'list' | 'loaded'>; 
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
					<NotificationsList selectionCallback={this.onSelectionItem}
						list={list.map<NotificationData>(({message, meetingTime, auditoryId}) => {
							return { 
								place: `Аудитория ${auditoryId}`, 
								time: meetingTime,
								message: message.length <= 0 ? 'Описание отсутсвует' : message, 
							}
						})} />
					)
				case 'failed':
					return createNoDataContent((
						<div style={{alignSelf: 'center', justifyItems: 'center', textAlign: 'center'}}>
							<h3 style={{color: 'crimson'}}>Не удалось загрузить приглашение</h3>
							<p>Попробуйте еще раз чуть позже</p>
						</div>
					))
				case 'empty':
					return createNoDataContent((
						<div style={{alignSelf: 'center', justifyItems: 'center', textAlign: 'center'}}>
							<h3 style={{color: '#333'}}>Список уведомлений пуст</h3>
							<p>Попробуйте еще раз чуть позже</p>
						</div>
					))
				case 'process': return createNoDataContent((<CircularProgress/>))
			}
		} 
		const { list, error, loaded, email } = this.state;
		return (
			<div style={{padding: '80px 0px'}}>
				<EmailInput changeCallback={(value) => this.onEmailChanged(value)} value={email ?? ''}/>
				<AlertsInfo info='Test' defaultState={error} />
				<h1 style={{textAlign: 'center', margin: '30px 0px 40px'}} className='fs-2 fw-medium'>
					Проверка уведомлений
				</h1>
				<MainContent list={list} loaded={loaded}/>
			</div>
		);
	}
}
export default NotificationsPage;