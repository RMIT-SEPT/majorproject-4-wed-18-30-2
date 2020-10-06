import React from 'react';
import '../App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Button, Form, FormGroup, Label, Input } from 'reactstrap';
import config from '../Constants';
import Booking from './modals/deleteService';
import AddService from './modals/addService';

class Dashboard extends React.Component {

  constructor(props) {
    super(props)
  }

  getServices() {

    const data = encodeURI('auth-token=' + localStorage.getItem('auth_token'));

    fetch(config.APP_URL + 'service/getall', {
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

    for (var k of Object.values(res)) {
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

      document.getElementById('tblData').innerHTML += "<tr>" +
        "<td>" + id + "</td>" +
        "<td>" + type + "</td>" +
        "<td>" + name + "</td>" +
        "</tr>";

      document.getElementById('service').innerHTML += "<option id='" + id + "'>" + name + "</option>";

    };

  }

  handleService(e) {

    var select = document.getElementById('service');

    window.selectedService = select.options[select.selectedIndex];

    document.getElementById('worker').innerHTML = "";
    window.selectedWorker = "";

    for (var k of Object.values(window.datas)) {

      if (k.id == window.selectedService.id) {

        for (var g of Object.values(k.workers)) {

          if (window.selectedWorker == "") {
            window.selectedWorker = g.id;
          }

          document.getElementById('worker').innerHTML += "<option id='" + g.id + "'>" + g.firstName + "</option>";

        };

      }

    };

  }

  handleWorker(e) {

    var select = document.getElementById('worker');

    window.selectedWorker = select.options[select.selectedIndex];

  }

  componentWillMount() {
    this.getServices();
    this.getUserId();
  }

  getUserId() {

    const data = encodeURI('auth-token=' + localStorage.getItem('auth_token'));

    fetch(config.APP_URL + 'auth/getuser', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      body: data
    })
      .then(res => res.json())
      .then(res => {

        for (var k in res) {
          if (res.hasOwnProperty(k)) {

            if (res[k]['id']) {
              localStorage.setItem('user_id', res[k]['id']);
            }

          }
        }

      });

  }

  showErrorModal(msg) {

    const node = document.getElementById('errorMessage');
    node.innerHTML = msg;
    node.classList.remove('d-none');

  }

  handleSubmit(event) {

    event.preventDefault();

    var date = document.getElementById('date').value;
    var time = document.getElementById('time').value;
    var my_user_id = localStorage.getItem('user_id');
    var worker_id = window.selectedWorker;
    var datetime = date + "T" + time + ":00.000Z[UTC]";

    const data = encodeURI('auth-token=' + localStorage.getItem('auth_token') + '&service-id=' + window.selectedService.id + '&booking-date=' + datetime + "&customer-id=" + my_user_id + "&worker-id=" + worker_id);

    fetch(config.APP_URL + 'booking/create', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      body: data
    })
      .then(res => res.json())
      .then(res => {

        if (typeof res.error != "undefined") {
          this.showErrorModal(res.error);
        } else {
          window.location = 'bookings';
        }

      });

  }

  render() {

    return (
      <div class="container">

        <h1 class="text-center">CUSTOMER</h1>

        <a href="bookings" class="btn btn-primary float-right">All Bookings</a>

        <div class="container">
          <h1 id='title'>List of services</h1>
          <table id='datas'>
            <tbody id="tblData">
              <tr>
                <th>ID</th>
                <th>Type</th>
                <th>Name</th>
              </tr>
            </tbody>
          </table>

          <br />
          <br />
          <br />
          <h1>Booking</h1>

          <Form className="" onSubmit={this.handleSubmit}>

            <div className="alert alert-danger d-none" id="errorMessage">
            </div>

            <FormGroup>
              <Label>Date</Label>
              <Input type="date" id="date" name="date" placeholder="Date and Time"></Input>
            </FormGroup>

            <FormGroup>
              <Label>Time (UTC)</Label>
              <Input type="time" id="time" name="time" placeholder="Time"></Input>
            </FormGroup>

            <FormGroup>
              <Label for="service">Services</Label>
              <Input type="select" name="service" id="service" onChange={this.handleService}>
                <option value=""></option>
              </Input>
            </FormGroup>

            <FormGroup>
              <Label for="worker">Worker</Label>
              <Input type="select" name="worker" id="worker" onChange={this.handleWorker}>

              </Input>
            </FormGroup>

            <Button className="btn-lg btn-success btn-block mt-5 mb-3" type="submit">
              Book
          </Button>

          </Form>

        </div>
      </div>

    )
  }

}

export default Dashboard;
