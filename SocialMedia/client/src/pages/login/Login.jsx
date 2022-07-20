import { useContext, useRef } from "react";
import "./login.css";
import { loginCall } from "../../apiCalls";
import { AuthContext } from "../../context/AuthContext";
import { CircularProgress } from "@material-ui/core";
import { Link } from "react-router-dom";

export default function Login() {
  const phonenumber = useRef();
  const password = useRef();
  const { isFetching, dispatch } = useContext(AuthContext);

  const handleClick = (e) => {
    e.preventDefault();
    loginCall(
      { phonenumber: phonenumber.current.value, password: password.current.value },
      dispatch
    );
  };

  return (
    <div className="login" >
      <div className="loginWrapper">
        <div className="loginLeft">
          <h3 className="loginLogo">Midusocial</h3>
          <span className="loginDesc">
            Nơi chia sẻ cuộc sống của bạn với mọi người
          </span>
        </div>
        <div className="loginRight">
          <form className="loginBox" onSubmit={handleClick}>
            <input
              placeholder="Phone number"
              type="number"
              required
              className="loginInput"
              ref={phonenumber}
            />
            <input
              placeholder="Password"
              type="password"
              required
              minLength="6"
              className="loginInput"
              ref={password}
            />
            <button className="loginButton" type="submit" disabled={isFetching}>
              {isFetching ? (
                <CircularProgress color="white" size="20px" />
              ) : (
                "Đăng nhập"
              )}
            </button>
            <span className="loginForgot">Quên mật khẩu?</span>

            {isFetching ? (
              <CircularProgress color="white" size="20px" />
            ) : (
              <Link style={{textAlign:"center", decoration: 'none', color: 'white' }} to='/register'>
                <button type="reset" className="loginRegisterButton">Đăng ký tài khoản mới
                </button></Link>
            )}

          </form>
        </div>
      </div>
    </div>
  );
}
