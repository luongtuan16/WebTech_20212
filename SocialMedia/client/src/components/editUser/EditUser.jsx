import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import React, { useContext, useState } from 'react';
import { SUCCESS_CODE } from '../../constraints';
import { AuthContext } from '../../context/AuthContext';
import { userRequest } from '../../requestMethods';
import './editUser.css';
export default function EditUser({ onUpdate, user, token }) {
    const [name, setName] = useState(user.username || '');
    const [desc, setDesc] = useState(user.description || '');
    const [city, setCity] = useState(user.city || '');
    const [address, setAddress] = useState(user.address || '');
    const [avatar, setAvatar] = useState(null);
    const [cover, setCover] = useState(null);
    const { user: currentUser, dispatch } = useContext(AuthContext);
    const [open, setOpen] = useState(false);
    const PF = process.env.REACT_APP_PUBLIC_FOLDER;
    const handleClickOpen = () => {
        setOpen(true);
    }
    const handleClose = () => {
        setOpen(false);
    }
    const handleSubmit = async () => {
        try {
            const data = new FormData();
            //const fileName = Date.now() + file.name;
            data.append("username", name);
            data.append("description", desc || '');
            data.append("city", city || '');
            data.append("address", address || '');
            if (avatar)
                data.append("avatar", avatar);
            if (cover)
                data.append("cover_image", cover);

            const editUser = await userRequest(token).post('/set_user_info', data);
            //console.log(editUser)
            if (editUser.data.code == SUCCESS_CODE) {
                setAvatar(null);
                setCover(null);
                dispatch({
                    type: "LOGIN_SUCCESS", payload: {
                        ...currentUser,
                        avatar: editUser.data.data.avatar
                    }
                })
                onUpdate();
            } else alert("Edit user failed");
        } catch (err) {
            console.log(err)
        }
        setOpen(false);
    }
    return (
        <div>
            <Button variant="outlined" onClick={handleClickOpen}>
                Edit User Information
            </Button>
            <Dialog open={open} onClose={handleClose}>
                <DialogTitle>Edit Informations</DialogTitle>
                <DialogContent>
                    <div className='edit-item'>
                        <span className='edit-input-lable'>Name</span>
                        <input value={name} onChange={e => setName(e.target.value)}
                            className='edit-input-text' placeholder='Your name' />
                    </div>
                    <div className='edit-item'>
                        <span className='edit-input-lable'>City</span>
                        <input value={city} onChange={e => setCity(e.target.value)}
                            className='edit-input-text' placeholder='Which city are you in?' />
                    </div>
                    <div className='edit-item'>
                        <span className='edit-input-lable'>Address</span>
                        <input value={address} onChange={e => setAddress(e.target.value)}
                            className='edit-input-text' placeholder='Your address' />
                    </div>
                    <div className='edit-item'>
                        <span className='edit-input-lable'>Description</span>
                        <input value={desc} onChange={e => setDesc(e.target.value)}
                            className='edit-input-text' placeholder='Some thing about you' />
                    </div>
                    <div className='edit-item'>
                        <span className='edit-input-lable'>Avatar</span>
                        <input
                            onChange={e => setAvatar(e.target.files[0])}
                            className='edit-input-file'
                            accept=".png,.jpeg,.jpg" type='file' />
                        {avatar && <img className='edit-image' src={URL.createObjectURL(avatar)} />}
                    </div>
                    <div className='edit-item'>
                        <span className='edit-input-lable'>Cover image</span>
                        <input
                            type="file"
                            accept=".png,.jpeg,.jpg"
                            onChange={e => setCover(e.target.files[0])}
                            className='edit-input-file' />
                        {cover && <img className='edit-image' src={URL.createObjectURL(cover)} />}
                    </div>

                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose}>Cancel</Button>
                    <Button onClick={handleSubmit}>Edit</Button>
                </DialogActions>
            </Dialog>
        </div>

    )
}
