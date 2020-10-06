import React from 'react';
import '../App.css';
import { Button, Form, FormGroup, Label, Input } from 'reactstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import config from '../Constants';


class SignUp extends React.Component {

  constructor() {
    super();
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  showErrorModal(msg) {

    const node = document.getElementById('errorMessage');
    node.innerHTML = msg;
    node.classList.remove('d-none');

  }

  handleResponse(resp) {

    if (typeof resp.error != "undefined") {
      this.showErrorModal(resp.error);
    } else if (typeof resp['auth-token'] != "undefined") {
      window.location = 'sign-in';
    }

  }

  handleSubmit(event) {

      event.preventDefault();
      const data = new URLSearchParams(new FormData(event.target));

      fetch(config.APP_URL + 'auth/register', {
        method: 'POST',
        body: data
      })
        .then(res => res.json())
        .then(res => this.handleResponse(res))

  }

  render() {


    return (
      <Form className="signup-form" onSubmit={this.handleSubmit} data-testid="form">
        <h1 className="font-weight-bold" id="heading">Sign Up</h1>

        <div className="alert alert-danger d-none" id="errorMessage">

        </div>

        <FormGroup>
          <Label>First Name</Label>
          <Input id="firstname" type="text" name="firstname" placeholder="First Name" ref={node => (this.firstname = node)}></Input>
        </FormGroup>

        <FormGroup>
          <Label>Last Name</Label>
          <Input id="lastname" type="text" name="lastname" placeholder="Last Name" ref={node => (this.lastname = node)}></Input>
        </FormGroup>

        <FormGroup>
          <Label>Email</Label>
          <Input id="email" type="email" name="email" placeholder="Email" ref={node => (this.email = node)}></Input>
        </FormGroup>

        <FormGroup>
          <Label>Password</Label>
          <Input id="password" type="password" name="password" placeholder="Password" ref={node => (this.password = node)}></Input>
        </FormGroup>

        {/*Do not forget to add this feature below after registration completed  */}
        {/* <FormGroup>
          <Label>Contact Number</Label>
          <Input type="text" placeholder="Contact Number"></Input>
        </FormGroup>
  
        <FormGroup>
          <Label>Address</Label>
          <Input type="text" placeholder="Address"></Input>
        </FormGroup>
  
        <FormGroup>
          <Label>Gender</Label>
          <br />
          <select id="gender" name="gender">
            <option value="Male">Male</option>
            <option value="Female">Female</option>
            <option value="Potato">Potato</option>
          </select>
        </FormGroup>
  
        <FormGroup>
          <Label>Date of Birth</Label>
          <Input type="date" placeholder="DOB"></Input>
        </FormGroup> */}



        {/* TODO : Add confirm password feature */}
        {/* <FormGroup>
          <Label>Confirm Password</Label>
          <Input type="password" placeholder="Confirm Password"></Input>
        </FormGroup> */}

        <Button className="btn-lg btn-success btn-block mt-5 mb-3" type="submit">
          Sign Up
        </Button>

        <div className="text-center">
          <a href="/sign-in">Sign up</a>
          <span className="p-2">|</span>
          <a href="/">Forgot Password</a>
        </div>


      </Form>
    );

  }

}

export default SignUp;
