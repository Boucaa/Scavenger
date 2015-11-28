var crypto = require('crypto'),
    algorithm = 'aes-256-ecb',
    password = 'd6F3Efeqd6F3Efeq';

function encrypt(text){
  var cipher = crypto.createCipher(algorithm,password)
  var crypted = cipher.update(text,'utf8','utf8')
  crypted += cipher.final('utf8');
  return crypted;
}
 
function decrypt(text){
  var decipher = crypto.createDecipher(algorithm,password)
  var dec = decipher.update(text,'hex','utf8')
  dec += decipher.final('utf8');
  return dec;
}

console.log(encrypt("hello Kappa"));