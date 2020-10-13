import React from "react";
import { mount } from "enzyme";
import ReactDOM from 'react-dom';
import Enzyme from "enzyme";
import Adapter from "enzyme-adapter-react-16";
import renderer from 'react-test-renderer';


//? Pages
import Bookings from "../bookings";
import {Link} from "react-router-dom";


it('renders correctly', () => {
    const tree = renderer
        .create(<Bookings page="/bookings">Bookings</Bookings>)
        .toJSON();
    expect(tree).toMatchSnapshot();
});




Enzyme.configure({ adapter: new Adapter() });

describe('Booking page test', () =>{
    //? 1. Check if Sign-up Page Render Properly
    test('renders learn react link', () => {
        const div = document.createElement('div');
        ReactDOM.render(<Bookings />, div);
    });
;

});