import { SUCCESS_CODE } from "./constraints";
import { publicRequest } from "./requestMethods";

export const loginCall = async (userCredential, dispatch) => {
  dispatch({ type: "LOGIN_START" });
  try {
    const res = await publicRequest.post("/login", userCredential);
    if (res.data.code == SUCCESS_CODE) {
      console.log(res.data.data)
      dispatch({ type: "LOGIN_SUCCESS", payload: res.data.data });
      //dispatch({ type: "LOGIN_SUCCESS", payload: {email: "us"} });
    }
    else {
      dispatch({ type: "LOGIN_FAILURE", payload: res.data.message })
      alert(res.data.message);
    }
  } catch (err) {
    dispatch({ type: "LOGIN_FAILURE", payload: err });
    alert(err.message);
  }
};
// export const loginCall = async (userCredential, dispatch) => {
//   dispatch({ type: "LOGIN_START" });
//   try {
//     const res = await axios.post("localhost/auth/login", userCredential);
//     dispatch({ type: "LOGIN_SUCCESS", payload: res.data });


//   } catch (err) {
//     dispatch({ type: "LOGIN_FAILURE", payload: err });
//     alert("Đăng nhập thất bại!");

//   }
// };

export const logoutCall = async (dispatch) => {
  try {
    dispatch({ type: "LOGOUT_SUCCESS" });

  }
  catch (err) {
    dispatch({ type: "LOGOUT_FAILURE" });
    alert("Đăng xuất thất bại!");

  }
}

// export const getFriends = async (userId, dispatch) => {
//   dispatch({ type: "LOGIN_START" });
//   try {
//     const res = await publicRequest.post("/login", userCredential);
//     if (res.data.code == SUCCESS_CODE){
//       //console.log(res.data.data)
//       dispatch({ type: "LOGIN_SUCCESS", payload: res.data.data });
//     }
//     else {
//       dispatch({ type: "LOGIN_FAILURE", payload: res.data.message })
//       alert(res.data.message);
//     }
//   } catch (err) {
//     dispatch({ type: "LOGIN_FAILURE", payload: err });
//     alert(err.message);
//   }
// };