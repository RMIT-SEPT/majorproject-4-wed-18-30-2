import React from "react";
import { mount } from "enzyme";
import ReactDOM from 'react-dom';
import Enzyme from "enzyme";
import Adapter from "enzyme-adapter-react-16";
import renderer from 'react-test-renderer';


//? Pages
import Profile from "../profile";
import {Link} from "react-router-dom";

it('renders correctly', () => {
    const tree = renderer
        .create(<Profile page="/bookings">Bookings</Profile>)
        .toJSON();
    expect(tree).toMatchSnapshot();
});