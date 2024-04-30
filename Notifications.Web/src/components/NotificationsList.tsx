import { DataGrid, GridColDef } from '@mui/x-data-grid';
import React from 'react';
import { Col, Container, Row } from 'react-bootstrap';

export type NotificationData = {
    readonly status: string;
    readonly name: string;
};
export type NotificationProps = {
    list: NotificationData[],
    selectionCallback: (index: number) => void
}
const columns: GridColDef[] = [
	{ field: 'status', headerName: 'Статус', width: 120, resizable: false },
	{ field: 'name', headerName: 'Название', width: 240, flex: 1 },
];
export function NotificationsList(props: NotificationProps): React.JSX.Element {
    const mappedList = props.list.map((item, index) => {
        return {id: index, ...item}
    })
    const initState = {
        pagination: {
            paginationModel: { pageSize: 10 },
        },
    };
    return (
    <div style={{margin: '20px 0px'}}>
    <Container fluid='md'>
        <Row className='justify-content-center'>
            <Col md={10} sm={12} lg={8}>
            <DataGrid rows={mappedList} columns={columns} disableMultipleRowSelection
                onRowClick={item => props.selectionCallback(item.id.valueOf() as number)}
                initialState={initState} pageSizeOptions={[10]}
            />
            </Col>
        </Row>
    </Container>
    </div>
    );
}