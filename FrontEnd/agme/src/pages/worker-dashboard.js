import React from 'react';
import '../App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Button, Form, FormGroup, Label, Input } from 'reactstrap';
import config from '../Constants';

class Bookings extends React.Component {

  constructor(props) {
    super(props)
  }

  renderTableData(data) {

    for (var k of Object.values(data)) {

      const { id, service_name, customer_name, datetime, del } = k

      document.getElementById('tblData').innerHTML += "<tr>" +
        "<td>" + id + "</td>" +
        "<td>" + service_name + "</td>" +
        "<td>" + customer_name + "</td>" +
        "<td>" + datetime + "</td>" +
        "</tr>";

    };

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
              this.getBookings();
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

  handleClick = (e) => {
    e.preventDefault();
    console.log('The link was clicked.');
  };

  getBookings() {

    var my_user_id = localStorage.getItem('user_id');

    const data = encodeURI('auth-token=' + localStorage.getItem('auth_token') + '&worker-id=' + my_user_id);

    fetch(config.APP_URL + 'booking/findByWorker', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      body: data
    })
      .then(res => res.json())
      .then(res => {

        var parsedData = [];

        for (var k of Object.values(res)) {

          var baddatetime = k.bookingTimestamp;
          var parts = baddatetime.split("T");
          var subparts = parts[1].split(":");
          var gooddate = parts[0];
          var goodtime = subparts[0] + ":" + subparts[1];
          var readabledatetime = (gooddate + " " + goodtime).replace('Z[UTC]', '').replace('Z[U', '');

          parsedData[k.id] = {
            "id": k.id,
            "service_name": k.serviceId.name + " (" + k.serviceId.type + ")",
            "customer_name": k.serviceId.workers[0].firstName,
            "datetime": readabledatetime
          };

        }

        this.renderTableData(parsedData);

      });

  }

  componentWillMount() {
    this.getUserId();
  }

  handleSubmit(event) {
    event.preventDefault();
    var bid = document.getElementById('booking-id').value;


    const data = encodeURI('auth-token=' + localStorage.getItem('auth_token') + '&booking-id=' + bid);

    fetch(config.APP_URL + 'booking/delete', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      body: data
    })
      .then(res => res.json())
      .then(window.location = "worker-dashboard")
  }

  render() {

    return (

      <div class="container">
        <h1 id='title'>List of Bookings for workers</h1>
        <table id='datas'>
          <tbody id="tblData">
            <tr>
              <th>ID</th>
              <th>Service</th>
              <th>Worker</th>
              <th>Date & Time</th>
            </tr>
          </tbody>
        </table>
        <div>
          <Form className="login-form" onSubmit={this.handleSubmit}>
            <div className="alert alert-danger d-none" id="errorMessage">
            </div>
            <div className="alert alert-success d-none" id="successMessage"></div>
            <FormGroup>
            </FormGroup>
          </Form>
        </div>
      </div>

    )
  }

}

export default Bookings;