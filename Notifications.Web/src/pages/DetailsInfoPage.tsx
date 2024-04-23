/* eslint-disable @typescript-eslint/ban-types */
import { Button, CircularProgress } from '@mui/material';
import { NavigatorProps } from 'App';
import React from 'react';
import { Col, Container, Row } from 'react-bootstrap';
import { ArrowLeft } from 'react-bootstrap-icons';
import { NotificationsResponse } from './NotificationsPage';

export interface DetailsInfoPageProps extends NavigatorProps {
}
export interface DetailsInfoPageState {
	info: NotificationsResponse | null,
	loaded: 'complete' | 'failed' | 'process'
}
class DetailsInfoPage extends React.Component<DetailsInfoPageProps, DetailsInfoPageState> {
	private locationData: { uuid: string } | null = null;
    public constructor(props: DetailsInfoPageProps) {
		super(props);
		this.state = { info: null, loaded: 'process' }

		const { state } = this.props.location;
		this.locationData = (state as { uuid: string })
	}
	private getDetailsInfo = async function(this: DetailsInfoPage, uuid: string)
		: Promise<NotificationsResponse> {
			const url = `http://localhost:8080/api/getByUuid?uuid=${uuid}`;
			const result = await fetch(url, {method: 'GET'});
			if(!result.ok) {
				throw new Error('Данные не получены')
			}
			return await result.json();
	}
	public override componentDidMount(): void {
		if(this.locationData == null) throw 'Данные не были отправлены';
		this.setDelailsInfo(this.locationData?.uuid)
	}
	private setDelailsInfo = (uuid: string): void => {
		this.getDetailsInfo(uuid)
			.then(value => {
				this.setState({ loaded: 'complete', info: value })
			})
			.catch(() => this.setState({ loaded: 'failed' }))
	}
	private onBackClicked = (): void => this.props.navigator('/', undefined);
	private onRefreshClicked = (): void => {
		if(this.locationData == null) throw 'Данные не были отправлены';
		this.setDelailsInfo(this.locationData?.uuid)
	}
	private onAgreedClicked = (accept: boolean): void => {
		if(this.locationData == null) throw 'Данные не были отправлены';
		const { uuid } = this.locationData;
		const status = accept ? 'ACCEPTED' : 'REJECTED'
		const url = `http://localhost:8080/api/meeting/status?status=${status}&uuid=${uuid}`
		fetch(url, { method: 'GET' })
			.then((value) => {
				this.setState({loaded: 'complete'})
				if(!value.ok) throw new Error('Данные не получены')
			})
			.then(() => this.props.navigator('/', undefined))
			.catch(() => this.setState({loaded: 'complete'}))
		this.setState({loaded: 'process'})
	}
	public override render(): React.ReactNode {
		const MainContent: React.FC<DetailsInfoPageState> = (props: DetailsInfoPageState) => {
			const { info, loaded } = props
			let content: React.JSX.Element | undefined = undefined;
			const message = (info?.message as string)
			switch (loaded) {
				case 'complete': content = ( 
					<div style={{alignSelf: 'self-start', width: '100%'}}>
						<h3>Приглашение на встречу</h3>
						<hr/>
						<div style={{margin: '0px 0px 30px'}}>
							<p>Время: {info?.meetingTime}</p>
							<p>Место: аудитория №{info?.auditoryId}</p>
							<p>Описание: {message.length <= 0 ? 'описание отсутствует': message}</p>
						</div>
						<Button color='primary' variant="contained" onClick={() => this.onAgreedClicked(true)}>
							Согласиться
						</Button>
						<Button variant="contained" color='warning' style={{marginLeft: '20px'}} 
							onClick={() => this.onAgreedClicked(false)}>
							Отказаться
						</Button>
					</div>
				)
				break;	
				case 'failed': content = (
					<div style={{alignSelf: 'center', justifyItems: 'center', textAlign: 'center'}}>
						<h3 style={{color: 'crimson'}}>Не удалось загрузить приглашение</h3>
						<p style={{marginBottom: '40px'}}>Попробуйте еще раз чуть позже</p>
						<Button variant="contained" onClick={this.onRefreshClicked}>Попробовать</Button>
					</div>
				); 
				break;
				case 'process': content = (<CircularProgress/>); break;
			}
			return (
			<div style={{
				boxShadow: '0px 0px 10px #AAA',
				borderRadius: '15px',
				display: 'flex',
				padding: '40px 20px',
				justifyContent: 'center',
				alignItems: 'center',
				minHeight: '500px'
			}}>{content}</div>
			)
		}
		const { info, loaded } = this.state
		return (
			<div style={detailsContentStyle}>
				<div style={detailsHeaderStyle}>
					<button onClick={this.onBackClicked} style={backButtonStyle}>
						<ArrowLeft width={24} height={24}/>
					</button>
					<div style={detailsHeaderTitleStyle}>
						<h1 style={{margin: '0', font: '18px sans-serif',}}>Просмотр уведомления</h1>
					</div>
				</div>
				<Container fluid="sm">
					<Row className='justify-content-center'>
						<Col sm={12} md={10} lg={8}>
							<MainContent info={info} loaded={loaded} />
						</Col>
					</Row>
				</Container>
			</div>
		);
	}
}
const detailsContentStyle: React.CSSProperties = {
	display: 'flex',
	flexFlow: 'column nowrap',
	alignItems: 'flex-start',
	margin: '100px 0px'
}
const detailsHeaderStyle: React.CSSProperties = {
	backgroundColor: '#227eb3', 
    padding: '18px', 
    position: 'fixed',
    top: 0,
    left: 0,
    width: '100%',
    zIndex: 1,
	display: 'flex',
	flexDirection: 'row'
}
const detailsHeaderTitleStyle: React.CSSProperties = {
	color: 'white',
	display: 'flex',
	flexGrow: 1,
	alignSelf: 'center',
	justifyContent: 'center'
}
const backButtonStyle: React.CSSProperties = {
	border: 'none',
	backgroundColor: 'transparent',
	margin: '0px 0px 0px 10px',
	padding: '0px'
}
export default DetailsInfoPage;
