const ContentHeading = () => {
    const generalStyle = {
      display: 'flex',
      alignItems: 'center',
      marginTop: '15px',
      marginLeft: '30px',
    };
  
    const mainTextStyle = {
      fontFamily: 'Poppins',
      fontSize: '18px',
      fontWeight: 500,
      lineHeight: '23px',
      letterSpacing: '0em',
      textAlign: 'left',
      color: 'hsla(221, 31%, 19%, 1)'
    };
  
    const fullTextStyle = {
      fontFamily: 'Poppins',
      fontSize: '12px',
      fontWeight: 300,
      lineHeight: '23px',
      letterSpacing: '0em',
      textAlign: 'center',
      color: 'hsla(221, 31%, 19%, 1)'
    };
  
    return (
      <div style={generalStyle}>
        <div style={mainTextStyle}>
          <h6>Kullanıcı Düzenle |</h6>
        </div>
        <div style={fullTextStyle}>
          <p>Ana Sayfa > Kullanıcı işlemleri > Kullanıcı Düzenle</p>
        </div>
      </div>
    );
  };
  
  export default ContentHeading;
  