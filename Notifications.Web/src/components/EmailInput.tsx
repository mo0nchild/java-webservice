import React from 'react';
import { Button, Col, Container, Form, InputGroup, Row } from 'react-bootstrap';
import { Search } from 'react-bootstrap-icons';

import 'bootstrap/dist/css/bootstrap.min.css';

export interface EmailInputProps {
    changeCallback: (value: string) => void,
    value: string,
}
export function EmailInput(props: EmailInputProps): React.JSX.Element {
    const inputControl = React.useRef<HTMLInputElement>(null);
    const onChange = () => {
        if(inputControl.current == null || inputControl.current.value.length <= 0) return;
        props.changeCallback(inputControl.current.value);
    }
    const { value } = props;
    return (
        <div style={{margin: '0px 0px 40px'}}>
        <Container fluid="md">
            <Row className="justify-content-center">
                <Col md={10} sm={12} lg={8}>
                    <InputGroup>
                        <Form.Control ref={inputControl}  type="email" defaultValue={value}
                            placeholder="name@example.com" />
                        <Button variant="light" className='border' onClick={onChange}>
                            <Search/>
                        </Button>
                    </InputGroup>
                </Col>
            </Row>
        </Container> 
        </div> 
    );
}
