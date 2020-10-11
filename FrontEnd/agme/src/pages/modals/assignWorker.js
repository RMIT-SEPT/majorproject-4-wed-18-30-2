import React from 'react';
import '../../App.css';
import { Button, Form, FormGroup, Label, Input } from 'reactstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import config from '../../Constants';

class AssignWorker extends React.Component {

    constructor() {
        super();
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    showErrorModal(msg) {

        const node = document.getElementById('errorMessage');
        node.innerHTML = msg;
        node.classList.remove('d-none');

    }

    showServiceCreated(msg) {

        const node = document.getElementById('successMessage');
        node.innerHTML = msg;
        node.classList.remove('d-none');

    }

    handleLoginResponse(resp) {

        if (typeof resp.error != "undefined") {
            this.showErrorModal(resp.error);
        } else {
            this.showServiceCreated("Service is Deleted");
            window.location = "admin-dashboard"
        }

    }

    getServices() {

        const data = encodeURI('auth-token=' + localStorage.getItem('auth_token'));

        fetch(config.APP_URL + 'auth/getuser', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: data
        })
            .then(res => res.json())
            .then(res => this.populateServices(res))

    }

    populateServices(res) {

        var parsedData = {};
        var index;
        var len;

        for (var k of Object.values(res[localStorage.getItem('user_id')].adminServices)) {
            parsedData[k.id] = {
                "id": k.id,
                "name": k.name,
                "type": k.type,
                "workers": k.workers
            };
        }

        window.datas = parsedData;

        this.renderTableData();

    }

    renderTableData() {

        for (var k of Object.values(window.datas)) {

            const { id, type, name, } = k

            if (window.selectedService == null) {
                window.selectedService = id;
            }

            document.getElementById('service').innerHTML += "<option id='" + id + "'>" + name + "</option>";

        };

    }

    componentWillMount() {
        this.getServices();
    }

    handleService(e) {

        var select = document.getElementById('service');

        window.selectedService = select.options[select.selectedIndex];


        for (var k of Object.values(window.datas)) {

            if (k.id == window.selectedService.id) {

                for (var g of Object.values(k.workers)) {

                    if (window.selectedWorker == "") {
                        window.selectedWorker = g.id;
                    }

                };

            }

        };

    }

    handleSubmit(event) {

        event.preventDefault();

        var uid = document.getElementById('user-id').value;

        const data = encodeURI('auth-token=' + localStorage.getItem('auth_token') + '&service-id=' + window.selectedService.id + '&user-id=' + uid);

        fetch(config.APP_URL + 'service/assignworker', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: data
        })
            .then(res => res.json())
            .then(res => this.handleLoginResponse(res))

    }



    render() {

        return (

            <Form className="login-form" onSubmit={this.handleSubmit}>
                <h1 className="font-weight-bold" id="heading">Assign Worker</h1>
                <div className="alert alert-danger d-none" id="errorMessage">
                </div>
                <div className="alert alert-success d-none" id="successMessage"></div>
                <FormGroup>
                    <Label for="service">Services</Label>
                    <Input type="select" name="service" id="service" onChange={this.handleService}>
                        <option value=""></option>
                    </Input>
                </FormGroup>

                <FormGroup>
                    <Label>User ID</Label>
                    <Input type="text" id="user-id" name="user-id" placeholder="User ID" ref={node => (this.userid = node)}></Input>
                </FormGroup>

                <Button className="btn-lg btn-success btn-block mt-5 mb-3" type="submit">
                    Assign
                </Button>
            </Form>
        );

    }

}

export default AssignWorker;
