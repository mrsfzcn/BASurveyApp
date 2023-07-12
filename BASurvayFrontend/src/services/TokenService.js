import jwt_decode from 'jwt-decode';

const TokenService = {
    decodeToken: (token) => {
        try {
            const decodedToken = jwt_decode(token);
            return decodedToken;
        } catch (error) {
            console.error('Error decoding token', error);
            return null;
        }
    },
}

export default TokenService;