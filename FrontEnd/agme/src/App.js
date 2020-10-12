import React from 'react';
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Route, BrowserRouter as Router, Switch } from "react-router-dom"
import SignIn from './pages/sign-in';
import SignUp from './pages/sign-up';
import Profile from './pages/profile';
import Home from './pages/home';
import Dashboard from './pages/customer-dashboard';
import Logout from './pages/logout';
import NotFoundPage from './pages/NotFoundPage' ;
import AddService from './pages/modals/addService';
import Bookings from './pages/bookings';
import AdminDashboard from './pages/admin-dashboard';
import AssignWorker from './pages/modals/assignWorker';
import DeleteService from './pages/modals/deleteService';
import WorkerDashboard from './pages/worker-dashboard';

function App() {
  return (
    <div id="main-container">
      <div id="content-wrap">
        <Router>
          <Switch>
            <Route path="/" exact component={Home} />
            <Route path="/sign-in" component={SignIn} />
            <Route path="/addservice" component={AddService} />
            <Route path="/sign-up" component={SignUp} />
            <Route path="/profile" component={Profile} />
            <Route path="/customer-dashboard" component={Dashboard} />
            <Route path="/dashboard" component={Dashboard} />
            <Route path="/bookings" component={Bookings} />
            <Route path="/logout" component={Logout} />
            <Route path="/admin-dashboard" component={AdminDashboard} />
            <Route path="/assignworker" component={AssignWorker} />
            <Route path="/deleteservice" component={DeleteService} />
            <Route path="/admin-dashboard" component={AdminDashboard} />
            <Route path="/worker-dashboard" component={WorkerDashboard} />

            <Route component={NotFoundPage} />
          </Switch>

        </Router>
      </div>
    </div>
  );
}

export default App;
