import React from "react";

function Header() {
    return (
        <div className="header">
            <nav className="navbar sticky-top navbar-dark bg-secondary">
                <a className="navbar-brand nav justify-content-center" href="/">AGME</a>
                <ul className="nav justify-content-right nav nav-pills">


                    {localStorage.getItem('auth_token') != null &&

                        <li className="nav-item">
                            <a className="navbar-brand nav justify-content-center" href="/admin-dashboard">Admin </a>
                        </li>

                    }

                    {localStorage.getItem('auth_token') != null &&

                        <li className="nav-item">
                            <a className="navbar-brand nav justify-content-center" href="/worker-dashboard">Worker </a>
                        </li>

                    }

                    {localStorage.getItem('auth_token') != null &&

                        <li className="nav-item">
                            <a className="navbar-brand nav justify-content-center" href="/customer-dashboard">Customer </a>
                        </li>

                    }

                    {localStorage.getItem('auth_token') != null &&

                        <li className="nav-item">
                            <a className="navbar-brand nav justify-content-center" href="/logout">Logout</a>
                        </li>

                    }

                    {localStorage.getItem('auth_token') == null &&

                        <li className="nav-item">
                            <a className="navbar-brand nav justify-content-center" href="/sign-in">Sign in</a>
                        </li>

                    }

                    {localStorage.getItem('auth_token') == null &&

                        <li className="nav-item">
                            <a className="navbar-brand nav justify-content-center" href="/sign-up">Sign up</a>
                        </li>

                    }

                </ul>
            </nav>
        </div>
    );
}

export default Header;