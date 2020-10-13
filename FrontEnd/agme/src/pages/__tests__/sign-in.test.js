import React from 'react';
import { render, screen } from '@testing-library/react';
import SignIn from '../pages/sign-in';
import describe from "riteway";

test('Sign In Page Title', () => {
  const { getByText } = render(<SignIn />);
  const linkElement = getByText(/Sign In/i);
  expect(linkElement).toBeInTheDocument();
});

describe('Sign In Button', () => {

  it('Sign In button must be present on the page', () => {

    render(<SignIn />);

    const button = screen.getByRole('button', { name: /Log In/i })
    expect(button).toBeInTheDocument();

  });

});

describe('Error Message Element', () => {

  it('The error message element should be present on the page, but hidden', () => {
    
    const { container } = render(<SignIn />);

    const errorMsg = container.querySelector('[id="errorMessage"]');
    expect(errorMsg.classList.contains('d-none')).toBe(true)

  });

});

describe('Error Message Content', () => {

  it('When an error is triggered, its content should match the alert message', () => {
    
    const { container } = render(<SignIn />);

    SignIn.prototype.showErrorModal("Account login error.");

    const errorMsg = container.querySelector('[id="errorMessage"]');
    expect(errorMsg.innerHTML).toBe('Account login error.')

  });

});

describe('Error Message Visibility', () => {

  it('When an error is triggered, it should be visible', () => {
    
    const { container } = render(<SignIn />);

    SignIn.prototype.showErrorModal("Account login error.");

    const errorMsg = container.querySelector('[id="errorMessage"]');
    expect(errorMsg.classList.contains('d-none')).toBe(false)

  });

});