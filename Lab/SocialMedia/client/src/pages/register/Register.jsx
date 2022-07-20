import axios from "axios";
import { useRef } from "react";
import "./register.css";
import { useHistory } from "react-router";
import { publicRequest } from "../../requestMethods";
import { SUCCESS_CODE } from "../../constraints";
import { Link } from "react-router-dom";

export default function Register() {
  const username = useRef();
  const phonenumber = useRef();
  const password = useRef();
  const passwordAgain = useRef();
  const history = useHistory();

  const handleClick = async (e) => {
    e.preventDefault();
    if (passwordAgain.current.value !== password.current.value) {
      passwordAgain.current.setCustomValidity("Passwords don't match!");
    } else {
      const user = {
        phonenumber: phonenumber.current.value,
        password: password.current.value,
      };
      try {
        const res = await publicRequest.post("/signup", user);
        if (res.data.code == SUCCESS_CODE) {
          history.push("/login");
        } else alert("Sign up fail");
      } catch (err) {
        console.log(err);
      }
    }
  };

  return (
    <div className="login">
      <div className="loginWrapper">
        <div className="loginLeft">
          <h3 className="loginLogo">Social Media</h3>
          <span className="loginDesc">
            Connect with friends and the world around you on Social Media.
          </span>
        </div>
        <div className="loginRight">
          <form className="loginBox" onSubmit={handleClick}>
            <input
              placeholder="Phone number"
              required
              ref={phonenumber}
              className="loginInput"
              type="phonenumber"
            />
            <input
              placeholder="Password"
              required
              ref={password}
              className="loginInput"
              type="password"
              minLength="6"
            />
            <input
              placeholder="Password Again"
              required
              ref={passwordAgain}
              className="loginInput"
              type="password"
            />
            <button className="loginButton" type="submit">
              Sign Up
            </button>
            <Link style={{ textAlign: "center", decoration: 'none', color: 'white' }} to='/login'>
              <button className="loginRegisterButton">
                Log into Account
              </button>
            </Link>
          </form>
        </div>
      </div>
    </div>
  );
}
