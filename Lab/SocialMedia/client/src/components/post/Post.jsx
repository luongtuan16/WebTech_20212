import "./post.css";
import { Box, Drawer, List, TextField } from "@material-ui/core";
import { Typography } from '@mui/material';
import Popover from '@mui/material/Popover';
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import { Fragment, useContext, useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { format } from "timeago.js";
import { AuthContext } from "../../context/AuthContext";
import Comment from './../comment/Comment';
import { userRequest } from "../../requestMethods";
import { SUCCESS_CODE } from "../../constraints";
export default function Post({ apost, onUpdate }) {
  const [like, setLike] = useState(apost.like);
  const [isLiked, setIsLiked] = useState(apost.is_liked);
  const [user, setUser] = useState(apost.author || {});
  const [visible, setVisible] = useState(false);
  const [visibleCap, setVisibleCap] = useState(false);
  const [text, setText] = useState('')
  const [visibleComment, setVisibleComment] = useState(false);
  const [comments, setComments] = useState([]);
  const [message, setMessage] = useState(null);
  const [post, setPost] = useState(apost);
  const [status, setStatus] = useState(false);
  const PF = process.env.REACT_APP_PUBLIC_FOLDER;
  const { user: currentUser } = useContext(AuthContext);

  const onhandleClick = (e) => {
    //console.log(visible)
    if (post.can_edit === "Can Edit")
      setVisible(e.target);
    else alert('Bạn không phải chủ bài viết này!')
  }
  const handleClose = () => {
    setVisible(null);
  };
  const handleCloseCap = () => {
    setVisibleCap(null);
  }
  const handleEditPost = async () => {
    if (text)
      try {
        const data = new FormData();
        data.append('id', post.id);
        data.append('described', text);
        const editPost = await userRequest(currentUser.token).post('/edit_post', data);
        if (editPost.data.code == SUCCESS_CODE) {
          setPost({ ...post, described: text });
          //alert('Sửa thành công!')
          setStatus(!status)
          setVisibleCap(null);
        } else alert("Update failed");
      }
      catch (err) {
        console.log(err);
      }
  }
  const callComment = async () => {
    try {
      const res = await userRequest(currentUser.token).get('/get_comment', {
        params: {
          id: post.id,
          index: 0,
          count: 10000
        }
      })
      //console.log(res.data);
      if (res.data.code != SUCCESS_CODE) {
        //alert('get comment fail');
        setComments([]);
      } else
        setComments(res.data.data);
    }
    catch (err) {
      console.log(err)
    }
    handleClickComment(true)
  }
  const addComment = async () => {
    if (!message || message.length === 0)
      return;
    const addComment = await userRequest(currentUser.token).get('/set_comment', {
      params: {
        id: post.id,
        comment: message
      }
    });
    //console.log(addComment)
    if (addComment.data.code == SUCCESS_CODE)
      callComment();
    else alert('Comment failed');
  }
  const onChangeText = (e) => {
    setText(e.target.value)
  }
  const handleClickComment = (logic) => {
    setVisibleComment(logic)
  }
  const onhandleDelete = async () => {
    if (post.can_edit) {
      const delPost = await userRequest(currentUser.token).get(`/delete_post?id=${post.id}`);
      if (delPost.data.code == SUCCESS_CODE) {
        alert('Xóa thành công!')
        //window.location.reload();
        onUpdate();
      } else alert("Update failed");
    }
    else {
      alert('Bạn không đủ thầm quyền!');
    }
  }
  const onhandleChange = (e) => {
    setVisible(null);
    setVisibleCap(e.target)
  }

  const likeHandler = () => {
    try {
      const likeResp = userRequest(currentUser.token).get(`/like?id=${post.id}`);
      if (likeResp.code = SUCCESS_CODE) {

        setLike(isLiked ? like - 1 : like + 1);
        setIsLiked(!isLiked);
      } else
        alert('Like failed');
    } catch (err) { console.log(err) }
  };
  const open = Boolean(visible);
  const id = open ? 'simple-popper' : undefined;
  const openCap = Boolean(visibleCap);
  const idCap = openCap ? 'simple-popper' : undefined;
  return (
    <div className="post">
      <div className="postWrapper">
        <div className="postTop">
          <div className="postTopLeft">
            <Link to={`/profile/${user.id}`}>
              <img
                className="postProfileImg"
                src={
                  user.avatar
                    ? PF + user.avatar.substring(user.avatar.lastIndexOf("\\") + 1, user.avatar.length)
                    : PF + "person/noAvatar.png"
                }
                alt=""
              />
            </Link>
            <span className="postUsername">{user.username}</span>
            <span className="postDate">{format(post.created)}</span>
          </div>
          <div className="postTopRight" >
            <button
              style={{
                border: "none",
                backgroundColor: "#1877f2",
                padding: "5px 10px",
                borderRadius: "8px",
                color: "white"
              }}
              aria-describedby={id} type="button" onClick={onhandleClick}>
              Xóa / Sửa
            </button>
            <Popover id={id} open={open} onClose={handleClose} anchorEl={visible} transformOrigin={{
              vertical: 'top',
              horizontal: 'left',
            }} anchorOrigin={{
              vertical: 'top',
              horizontal: 'left',
            }}>
              <Typography sx={{ p: 2 }} style={{ cursor: 'pointer' }} onClick={onhandleDelete}>Xóa</Typography>
              <Typography sx={{ p: 2 }} style={{ cursor: 'pointer' }} onClick={onhandleChange}>Sửa</Typography>

            </Popover>
            <Dialog open={visibleCap ? true : false} onClose={handleClose}>
              <DialogTitle>Edit Post Title</DialogTitle>
              <DialogContent>
                <TextField
                  autoFocus
                  margin="dense"
                  id="name"
                  label="Title"
                  type="text"
                  fullWidth
                  variant="standard"
                  defaultValue={post?.described}
                  onChange={e => onChangeText(e)}
                />
              </DialogContent>
              <DialogActions>
                <Button onClick={handleCloseCap}>Cancel</Button>
                <Button onClick={handleEditPost}>Edit</Button>
              </DialogActions>
            </Dialog>
          </div>
        </div>
        <div className="postCenter">
          <span className="postText">{post?.described}</span>
          {post.image?.map((img, idx) =>
            <img key={idx} className="postImg" src={PF + img.substring(img.lastIndexOf("\\") + 1, img.length)} alt="" />
          )}
        </div>
        <div className="postBottom">
          <div className="postBottomLeft">
            <img
              className="likeIcon"
              src='/images/like.png'
              onClick={likeHandler}
              alt=""
            />
            <img
              className="likeIcon"
              src='/images/heart.png'
              onClick={likeHandler}
              alt=""
            />
            <span className="postLikeCounter">{like} người thích điều này</span>
          </div>
          <div className="postBottomRight">
            <Fragment>
              <span className="postCommentText" onClick={callComment}> Bình luận ({post.comment})</span>
              <Drawer anchor={'right'}
                open={visibleComment}
                onClose={() => handleClickComment(false)}>
                <Box
                  role="presentation"
                >
                  <List style={{ width: '500px' }}>
                    <TextField
                      id="filled-multiline-static"
                      label="Thêm bình luận "
                      multiline
                      rows={6}
                      fullWidth
                      onChange={(e) => { setMessage(e.target.value) }}
                      variant="filled"
                    />
                    <button
                      style={{
                        margin: "10px",
                        padding: "10px",
                        border: "1px solid #1872f2",
                        borderRadius:"5px",
                        background:"white",
                        cursor:"pointer"
                      }}
                      onClick={addComment}>Bình luận</button>
                    {comments ? comments.map((comment, index) =>
                      <Comment postId={post.id} comment={comment} key={index} message={message} callComment={callComment} />) : <></>}
                  </List>
                </Box>
              </Drawer>
            </Fragment>

          </div>
        </div>
      </div>
    </div>
  );
}
