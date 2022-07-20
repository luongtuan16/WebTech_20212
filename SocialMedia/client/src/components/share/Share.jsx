import {
  Cancel, EmojiEmotions, Label, PermMedia, Room
} from "@material-ui/icons";
import { useContext, useRef, useState } from "react";
import { SUCCESS_CODE } from "../../constraints";
import { AuthContext } from "../../context/AuthContext";
import { userRequest } from "../../requestMethods";
import "./share.css";

export default function Share({ onUpdate }) {
  const { user } = useContext(AuthContext);
  const PF = process.env.REACT_APP_PUBLIC_FOLDER;
  const desc = useRef();
  const [file, setFile] = useState(null);
  
  const submitHandler = async (e) => {
    e.preventDefault();
    if (desc || file) {
      try {
        const data = new FormData();
        //const fileName = Date.now() + file.name;
        data.append("described", desc.current.value);
        if (file) {
          for (let i = 0; i < file.length; i++)
            data.append("image", file[i]);
        }
        const addPost = await userRequest(user.token).post('/add_post', data);
        console.log(addPost)
        if (addPost.data.code == SUCCESS_CODE) {
          setFile([]);
          desc.current.value = '';
          onUpdate();
        } else alert("Add post failed");
      } catch (err) {
        console.log(err)
      }
    }

  };

  const showFiles = () => {
    if (file) {
      const arr = new Array(file.length)
      for (let i = 0; i < arr.length; i++)
        arr[i] = i;
      //console.log(arr)
      //console.log(file[0])
      return arr?.map(idx =>
        <img key={idx} className="shareImg" src={URL.createObjectURL(file[idx])} alt="" />
      )
    }
  }
  return (
    <div className="share">
      <div className="shareWrapper">
        <div className="shareTop">
          <img
            className="shareProfileImg"
            src={
              user.avatar
                ? PF + user.avatar.substring(user.avatar.lastIndexOf("\\") + 1, user.avatar.length)
                : PF + "default_user.png"
            }
            alt=""
          />
          <input
            placeholder={"Bạn đang nghĩ gì vậy, " + user.username + "?"}
            className="shareInput"
            ref={desc}
          />
        </div>
        <hr className="shareHr" />
        {file?.length > 0 && (
          <div className="shareImgContainer">
            {showFiles()}
            <Cancel className="shareCancelImg" onClick={() => setFile(null)} />
          </div>
        )}
        <form className="shareBottom" onSubmit={submitHandler}>
          <div className="shareOptions">
            <label htmlFor="file" className="shareOption">
              <PermMedia htmlColor="tomato" className="shareIcon" />
              <span className="shareOptionText">Photo or Video</span>
              <input
                style={{ display: "none" }}
                type="file"
                id="file"
                accept=".png,.jpeg,.jpg"
                multiple
                onChange={(e) => setFile(e.target.files)}
              />
            </label>
            <div className="shareOption">
              <Label htmlColor="blue" className="shareIcon" />
              <span className="shareOptionText">Tag</span>
            </div>
            <div className="shareOption">
              <Room htmlColor="green" className="shareIcon" />
              <span className="shareOptionText">Location</span>
            </div>
            <div className="shareOption">
              <EmojiEmotions htmlColor="goldenrod" className="shareIcon" />
              <span className="shareOptionText">Feelings</span>
            </div>
          </div>
          <button className="shareButton" type="submit">
            Share
          </button>
        </form>
      </div>
    </div>
  );
}
