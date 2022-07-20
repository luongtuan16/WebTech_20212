import "./profile.css";
import Topbar from "../../components/topbar/Topbar";
import Sidebar from "../../components/sidebar/Sidebar";
import Feed from "../../components/feed/Feed";
import Rightbar from "../../components/rightbar/Rightbar";
import { useEffect, useState } from "react";
import axios from "axios";
import { useParams } from "react-router";
import { useContext } from "react";
import AddAPhotoIcon from '@mui/icons-material/AddAPhoto';
import { MoreVert } from "@material-ui/icons";
import { AuthContext } from './../../context/AuthContext';
import { userRequest } from "../../requestMethods";
import { SUCCESS_CODE } from "../../constraints";

export default function Profile() {
  const PF = process.env.REACT_APP_PUBLIC_FOLDER;
  const [user, setUser] = useState({});
  const id = useParams().id;
  const { user: currentUser } = useContext(AuthContext);
  const [file, setFile] = useState(null);
  const [update, setUpdate] = useState(false);
  const setImage = () => {

  }
  const setProfile = async (e) => {
    // console.log(e.target.files[0].name)
    // setFile(e.target.files[0]);
    // const data = new FormData();
    // const filename = Date.now() + e.target.files[0].name;
    // data.append("name", filename);
    // data.append("file", e.target.files[0]);
    // try {
    //   await axios.post("http://localhost:8800/api/upload", data);
    //   console.log('successima');
    // } catch (err) {
    //   console.log(err);
    // }
    // try {
    //   await axios.put(`http://localhost:8800/api/users/${currentUser._id}`, { profilePicture: filename, userId: currentUser._id });
    //   console.log('success')
    // }
    // catch (err) {
    //   console.log(err);
    // }
    // window.location.reload();
  }
  useEffect(() => {
    window.scrollTo(0, 0);
  }, [])
  useEffect(() => {
    const fetchUser = async () => {
      const res = await userRequest(currentUser.token).get(`/get_user_info?user_id=${id}`);
      if (res.data.code == SUCCESS_CODE) {
        //console.log(res.data);
        setUser(res.data.data);
      } else
        alert(res.data.message);
    };
    fetchUser();
  }, [id, update]);

  return (
    <>
      <Topbar />
      <div className="profile">
        <Sidebar />
        <div className="profileRight">
          <div className="profileRightTop">
            <div className="profileCover">
              <img
                className="profileCoverImg"
                src={
                  user.cover_image
                    ? PF + user.cover_image.substring(user.cover_image.lastIndexOf("\\") + 1, user.cover_image.length)
                    : PF + "default_cover.jpg"
                }
                alt=""
              />
              <label htmlFor="fileprofile" className="setImage" style={{ position: 'absolute', top: '250px', zIndex: '2' }} onClick={setImage}>
              </label>
              <input
                style={{ display: "none" }}
                type="file"
                id="fileprofile"
                accept=".png,.jpeg,.jpg"
                onChange={(e) => setProfile(e)}
              />{user.id == currentUser.id && <AddAPhotoIcon />}
              <img
                className="profileUserImg"
                src={
                  user.avatar
                    ? PF + user.avatar.substring(user.avatar.lastIndexOf("\\") + 1, user.avatar.length)
                    : PF + "default_user.png"
                }
                alt=""
              />
            </div>
            <div className="profileInfo">
              <h4 className="profileInfoName">{user.username}</h4>
              <span className="profileInfoDesc">{user.description}</span>
            </div>
          </div>
          <div className="profileRightBottom">
            <Feed userId={user.id} />
            <Rightbar onUpdate={() => setUpdate(!update)} user={user} />
          </div>
        </div>
      </div>
    </>
  );
}
