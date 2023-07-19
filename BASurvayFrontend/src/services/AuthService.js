import axios from "axios";

const LOGIN = "http://localhost:8090/api/v1/auth/authenticate";
const VERIFYCODE = "http://localhost:8090/api/v1/auth/verifycode";
const REGISTER = "http://localhost:8090/api/v1/auth/register";

class AuthService {
  login(user) {
    return axios.post(LOGIN, user);
  }
  verifyCode(code) {
    return axios.post(VERIFYCODE, code);
  }
  register(data){
    return axios.post(REGISTER,data);
  }

}

export default new AuthService();
