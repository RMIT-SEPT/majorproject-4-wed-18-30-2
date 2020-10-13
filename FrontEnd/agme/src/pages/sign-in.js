import React from 'react';
import '../App.css';
import { Button, Form, FormGroup, Label, Input } from 'reactstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import config from '../Constants';

class SignIn extends React.Component {

  constructor() {
    super();
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  showErrorModal(msg) {

    const node = document.getElementById('errorMessage');
    node.innerHTML = msg;
    node.classList.remove('d-none');
    
  }

  handleLoginResponse(resp) {

    if(typeof resp.error != "undefined") {
      this.showErrorModal(resp.error);
    } else if (typeof resp['auth-token'] != "undefined") {
      localStorage.setItem('auth_token', resp['auth-token']);
      this.redirectUser();
    }

  }

  redirectUser() {

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

        if(res[localStorage.getItem('user_id')].adminServices.length > 0) {
          window.location = 'admin-dashboard';
        } else if(res[localStorage.getItem('user_id')].workerServices.length > 0) {
          window.location = 'worker-dashboard';
        } else {
          window.location = 'customer-dashboard';
        }

      });

  }

  handleSubmit(event) {

    event.preventDefault();
    const data = new URLSearchParams(new FormData(event.target));

    fetch(config.APP_URL + 'auth/login', {
      method: 'POST',
      body: data
    })
      .then(res => res.json())
      .then(res => this.handleLoginResponse(res))

  }

  render() {

    return (
      <Form className="login-form" onSubmit={this.handleSubmit}>
        <h1 className="font-weight-bold" id="heading">Sign In</h1>

        <div className="alert alert-danger d-none" id="errorMessage">

        </div>

        <FormGroup>
          <Label>Email</Label>
          <Input type="email" name="email" placeholder="Email" ref={node => (this.email = node)}></Input>
        </FormGroup>

        <FormGroup>
          <Label>Password</Label>
          <Input type="password" name="password" placeholder="Password" ref={node => (this.password = node)}></Input>
        </FormGroup>

        <Button className="btn-lg btn-success btn-block mt-4 mb-3">
          Log In
      </Button>

        <div className="text-center">
          <a href="/sign-up">Sign up</a>
          <span className="p-2">|</span>
          <a href="/">Forgot Password</a>
        </div>

      </Form>
    );

  }

}

export default SignIn;
