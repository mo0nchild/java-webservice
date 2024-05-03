import React from 'react';
import { Alert, Col, Container, Row } from 'react-bootstrap';
import { ExclamationOctagon } from 'react-bootstrap-icons'

import 'bootstrap/dist/css/bootstrap.min.css';

export interface AlertsInfoProps {
    readonly info: string;
    readonly defaultState: boolean;
}
export function AlertsInfo(props: AlertsInfoProps): React.JSX.Element {
    const [show, setShow] = React.useState(props.defaultState);
    const alertContent = (
        <Container fluid='md'>
            <Row className="justify-content-md-center">
                <Col md='10' lg='6'>
                    <Alert variant='danger' onClose={() => setShow(false)} dismissible>
                        <ExclamationOctagon style={{marginRight: 10}}/>
                        {props.info}
                    </Alert>
                </Col>
            </Row>
        </Container>
        
    );
    return show ? <div style={{marginTop: 10}}>{alertContent}</div> : <div></div>
}