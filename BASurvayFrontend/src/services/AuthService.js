import axios from "axios";

const LOGIN = "http://localhost:8090/api/v1/auth/authenticate";

class AuthService{
    login(user){
        return axios.post(LOGIN,user);
    }
}

export default new AuthService();