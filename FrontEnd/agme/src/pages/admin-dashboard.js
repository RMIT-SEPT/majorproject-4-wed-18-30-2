import React from 'react';
import '../App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Button, Form, FormGroup, Label, Input } from 'reactstrap';
import config from '../Constants';
import Booking from './modals/deleteService';
import AddService from './modals/addService';

class AdminDashboard extends React.Component {

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

      const { id, type, name } = k

      if (window.selectedService == null) {
        window.selectedService = id;
      }

      document.getElementById('tblData').innerHTML += "<tr>" +
        "<td>" + id + "</td>" +
        "<td>" + type + "</td>" +
        "<td>" + name + "</td>" +
        
        "</tr>";

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

    const data = encodeURI('service-id=' + window.selectedService.id + '&booking-date=' + datetime + "&customer-id=" + my_user_id + "&worker-id=" + worker_id);

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
      <div>
        <h1>ADMIN</h1>

        <div class="container">
          <h1 id='title'>List of myservices</h1>
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

          <table id="test">
            <tr id="test">
              <th id="test">Description</th>
              <th id="test">Action</th>
            </tr>
            <tr id="test" >
              <th id="test">Add Service</th>
              <th id="test"><Button color="primary" href="/addservice">Add</Button></th>
            </tr>
            <tr>
              <th id="test">Assign Worker</th>
              <th id="test" ><Button color="info" href="/assignworker">Assign</Button></th>
            </tr>
            <tr id="test">
              <th id="test">Delete Service</th>
              <th id="test"><Button color="danger" href="/deleteservice">Delete</Button></th>
            </tr>
          </table>

          
        </div>

      </div>

    )
  }

}

export default AdminDashboard;
