import React from "react";
import { Container, Nav, Navbar } from "react-bootstrap";
import { Justify } from "react-bootstrap-icons";

export default function HeaderNavigation() {
    const [selected, setSelected] = React.useState('/');
    React.useEffect(() => {
        setSelected(window.location.pathname);
    }, [])
    return (
    <Navbar expand="md" style={EmailNavigationStyle}>
        <Container fluid='md'>
            <Navbar.Brand style={{color: 'white'}}>Уведомления</Navbar.Brand>
            <Navbar.Toggle style={{ width: '40px', padding: '0px', borderColor: 'white' }}>
                <Justify width={30} height={30} color="white"/>
            </Navbar.Toggle>
            <Navbar.Collapse id="basic-navbar-nav">
                <Nav className="me-auto">
                    <Nav.Link href="/" style={{
                        color: 'white', 
                        textUnderlineOffset: '5px',
                        textDecoration: selected == '/' ? 'underline' : 'none' 
                    }} >Список уведомлений</Nav.Link>
                    <Nav.Link href="/owner" style={{
                        color: 'white', 
                        textUnderlineOffset: '5px',
                        textDecoration: selected == '/owner' ? 'underline' : 'none' 
                    }} >Ваши мероприятия</Nav.Link>
                </Nav>
            </Navbar.Collapse>
        </Container>
    </Navbar>
    )
} 
const EmailNavigationStyle: React.CSSProperties = {
    backgroundColor: '#227eb3',
    position: 'fixed',
    top: 0,
    left: 0,
    width: '100%',
    zIndex: 1
};