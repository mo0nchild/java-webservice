/* eslint-disable @typescript-eslint/ban-types */
import { NavigatorProps } from 'App';
import React from 'react';
import { MeetingResponse } from './OwnerPage';
import { Button, CircularProgress } from '@mui/material';
import { Col, Container, Row } from 'react-bootstrap';
import { ArrowLeft } from 'react-bootstrap-icons';
import { DataGrid } from '@mui/x-data-grid';
export interface MeetingInfoPageProps extends NavigatorProps {
}
export interface MeetingInfoPageState {
	info: MeetingResponse | null,
	loaded: 'complete' | 'failed' | 'process'
}
const columns: GridColDef[] = [
	{ field: 'status', headerName: 'Статус', width: 120, resizable: false },
	{ field: 'email', headerName: 'Пользователь', width: 240, flex: 1 },
];
class MeetingInfoPage extends React.Component<MeetingInfoPageProps, MeetingInfoPageState> {
    private locationData: { uuid: string } | null = null;
    public constructor(props: MeetingInfoPageProps) {
        super(props);
        this.state = { info: null, loaded: 'process' }

		const { state } = this.props.location;
		this.locationData = (state as { uuid: string })
    }
    private getMeetingInfo = async function(this: MeetingInfoPage, uuid: string)
		: Promise<MeetingResponse> {
			const url = `http://localhost:8080/api/meeting/getByUuid?uuid=${uuid}`;
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
		this.getMeetingInfo(uuid)
			.then(value => {
				this.setState({ loaded: 'complete', info: value })
			})
			.catch(() => this.setState({ loaded: 'failed' }))
	}
    private onBackClicked = (): void => this.props.navigator('/owner', undefined);
	private onRefreshClicked = (): void => {
		if(this.locationData == null) throw 'Данные не были отправлены';
		this.setDelailsInfo(this.locationData?.uuid)
	}
    public override render(): React.ReactNode {
        const MainContent: React.FC<MeetingInfoPageState> = (props: MeetingInfoPageState) => {
			const { info, loaded } = props
			let content: React.JSX.Element | undefined = undefined;
            const peopleCount = info?.notifications.filter(item => item.status == 'ACCEPTED').length;
			const mappedList = props.info?.notifications.map((item, index) => {
                return {id: index, ...item}
            })
            const initState = {
                pagination: {
                    paginationModel: { pageSize: 5 },
                },
            };
            switch (loaded) {
				case 'complete': content = ( 
					<div style={{alignSelf: 'self-start', width: '100%'}}>
						<h3>Приглашение на встречу</h3>
						<hr/>
						<div style={{margin: '0px 0px 40px'}}>
							<p>Название: {props.info?.name}</p>
							<p>Организатор: {props.info?.ownerEmail}</p>
						</div>
						<div style={{margin: '0px 0px 40px'}}>
							<p>Время: {props.info?.meetingTime}</p>
							<p>Место: {props.info?.place}</p>
							<p>Описание: {props.info!.description.length <= 0 ? 'описание отсутствует': props.info!.description}</p>
						</div>
                        <div style={{margin: '0px 0px 40px'}}>
							<p>Количество соглашившихся: {peopleCount}</p>
                            <DataGrid rows={mappedList} columns={columns} disableMultipleRowSelection
                                initialState={initState} pageSizeOptions={[5]}/>
						</div>
					</div>
				)
				break;	
				case 'failed': content = (
					<div style={{alignSelf: 'center', justifyItems: 'center', textAlign: 'center'}}>
						<h3 style={{color: 'crimson'}}>Не удалось загрузить мероприятие</h3>
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
						<h1 style={{margin: '0', font: '18px sans-serif',}}>Просмотр мероприятия</h1>
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
export default MeetingInfoPage;