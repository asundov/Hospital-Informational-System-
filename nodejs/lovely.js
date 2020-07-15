const pool = require('../db.credential');

var nodemailer = require('nodemailer');




//register_patient
const register_patient = (req, res) => {

    const post_data = req.body; //get POST params

    const firstName = post_data.firstName;
    const lastName = post_data.lastName;
    const birthDate = post_data.birthDate;
    const email = post_data.email;
    const password = post_data.password;
    const cin = post_data.CIN;
    const maritalStatus = post_data.maritalStatus;

    pool.query('SELECT * FROM `Patient` WHERE `email` = ?', [email],
        (err, result) => {
            if (result && result.length) {
                res.json('User already exists!');
            } else {
                pool.query('INSERT INTO Patient(firstName, lastName, birthDate, password, email, CIN, maritalStatus) VALUES (?,?,?,?,?,?,?)',
                    [firstName, lastName, birthDate, password, email, cin, maritalStatus],
                    (err, result) => {
                        console.log(result);
                        res.json(result);
                    })
            }
        });
}




//register_doctor
const register_doctor = (req, res) => {

    const post_data = req.body; //get POST params

    const fullName = post_data.fullName;
    const code = post_data.code;
    const phoneNumber = post_data.phoneNumber;
    const email = post_data.email;
    const city = post_data.city;
    const address = post_data.address;
    const speciality = post_data.speciality;
    const password = post_data.password;


    pool.query('SELECT * FROM `Doctor` WHERE `code` = ?', [code],
        (err, result) => {

            if (result && result.length) {
                res.json('User already exists!');
            } else {
                pool.query('INSERT INTO Doctor(fullName, code, phoneNumber, email, city, address, speciality,password) VALUES (?,?,?,?,?,?,?,?)',
                    [fullName, code, phoneNumber, email, city, address, speciality, password],
                    (err, result) => {
                        console.log(result);
                        res.json(result);
                    })
            }
        });
}




//login
const login = (req, res) => {
    var post_data = req.body;

    //extract email and password from request
    var user_password = post_data.password;
    var email = post_data.email;

    pool.query('SELECT * FROM Patient where email=? and password=?', [email, user_password], function(err, result) {
        pool.on('error', function(err) {
            console.log('[MySQL ERROR]', err);
            res.JSON('Registraion error ', err);
        });


        if (result && result.length) {
            res.end(JSON.stringify(result[0])); //if password true, return all info on user

        } else {
            res.json('User does not exist!');
        }

    });
}



//get all person from database and search is key != ""
const search = (req, res) => {
    var post_data = req.body; //get post body
    var name = post_data.key; //get field 'search' from post data


    if (typeof name === 'undefined') {
        pool.query('SELECT * FROM User', function(error, result, fields) {
            pool.on('error', function(err) {
                console.log('[MYSQL]ERROR', err);
            });
            if (result && result.length) {
                res.end(JSON.stringify(result));
            } else {
                res.end(JSON.stringify('No person here'));
            }
        });
    } else {
        const nameContaines = '%' + name + '%';

        pool.query("SELECT * FROM User WHERE name LIKE ?", [nameContaines], function(err, result) {
            pool.on('error', function(err) {
                console.log('[MYSQL]ERROR', err);
            });
            if (result && result.length) {
                res.end(JSON.stringify(result));

            } else {
                res.end(JSON.stringify('No person here'));
            }
        });
    }
}




//reset password
const reset_password = (req, res) => {
    var post_data = req.body;

    //extract email and password from request
    var email = post_data.email;
    var new_password = post_data.new_password;

    pool.query('SELECT * FROM Patient where email=?', [email], function(err, result) {
        pool.on('error', function(err) {
            console.log('[MySQL ERROR]', err);
            res.JSON('Registraion error ', err);
        });

        if (result && result.length) {

            pool.query('UPDATE Patient SET password=?', [new_password], function(err, result) {
                pool.on('error', function(err) {
                    console.log('[MySQL ERROR]', err);
                    res.JSON('Registraion error ', err);
                });

                var transporter = nodemailer.createTransport({
                    service: 'gmail',
                    auth: {
                        user: 'ana.sundov1@gmail.com',
                        pass: 'jo72779O'
                    }
                });

                var mailOptions = {
                    from: 'ana.sundov1@gmail.com',
                    to: 'ana.sundov1@gmail.com',
                    subject: 'Reset password',
                    text: 'New password is ' + new_password
                };

                transporter.sendMail(mailOptions, function(error, info) {
                    if (error) {
                        console.log(error);
                    } else {
                        console.log('Email sent: ' + info.response);
                    }
                });
            });
        } else {
            res.json('User does not exist!');
        }
    });

}




//get_appointment
const get_appointment = (req, res) => {
    var post_data = req.body;

    //extract email and password from request
    var email = post_data.email;

    pool.query('SELECT * FROM Appointment where emailPatient=?', [email], function(err, result) {
        pool.on('error', function(err) {
            console.log('[MySQL ERROR]', err);
            res.JSON('Registraion error ', err);
        });

        if (result && result.length) {
            res.end(JSON.stringify(result)); //if password true, return all info on user

        } else {
            res.json('User does not exist!');
        }
    });
}


//get_doctor from email
const get_doctor = (req, res) => {
    var post_data = req.body;

    //extract email and password from request
    var email = post_data.emailDoctor;

    pool.query('SELECT * FROM Doctor where email=?', [email], function(err, result) {
        pool.on('error', function(err) {
            console.log('[MySQL ERROR]', err);
            res.JSON('Registraion error ', err);
        });

        console.log(JSON.stringify(result)); //if password true, return all info on user

        if (result && result.length) {
            res.end(JSON.stringify(result)); //if password true, return all info on user

        } else {
            res.json('User does not exist!');
        }
    });
}

//get_ALL_doctors
const get__all_doctors = (req, res) => {
    var post_data = req.body;

    //extract email and password from request
    var email = post_data.emailPatient;

    pool.query('SELECT * FROM Doctor', function(err, result) {
        pool.on('error', function(err) {
            console.log('[MySQL ERROR]', err);
            res.JSON('Registraion error ', err);
        });

        console.log(JSON.stringify(result)); //if password true, return all info on user

        if (result && result.length) {
            res.end(JSON.stringify(result)); //if password true, return all info on user

        } else {
            res.json('User does not exist!');
        }
    });
}


//add_appointment
const add_appointment = (req, res) => {

    const post_data = req.body; //get POST params

    const date = post_data.date;
    const time = post_data.time;
    const emailDoctor = post_data.emailDoctor;
    const emailPatient = post_data.emailPatient;
    const status = post_data.status;

    console.log("dne idje");

    pool.query('INSERT INTO Appointment(date, time, emailDoctor, emailPatient, status) VALUES (?,?,?,?,?)',
        [date, time, emailDoctor, emailPatient, status],
        function(err, result) {
            pool.on('error', function(err) {
                console.log('[MySQL ERROR]', err);
                res.JSON('Registraion error ', err);
            });

            res.end(JSON.stringify(result)); //if password true, return all info on user

        });
}


//get_hospitalisation
const get_hospitalisation = (req, res) => {
    var post_data = req.body;

    //extract email and password from request
    var email = post_data.emailPatient;

    pool.query('SELECT * FROM Hospitalisation where emailPatient=?', [email], function(err, result) {
        pool.on('error', function(err) {
            console.log('[MySQL ERROR]', err);
            res.JSON('Registraion error ', err);
        });

        if (result && result.length) {
            res.end(JSON.stringify(result)); //if password true, return all info on user

        } else {
            res.json('User does not exist!');
        }
    });
}

//get_consultation
const get_consultation = (req, res) => {
    var post_data = req.body;

    //extract email and password from request
    var email = post_data.emailPatient;

    pool.query('SELECT * FROM Consultation where emailPatient=?', [email], function(err, result) {
        pool.on('error', function(err) {
            console.log('[MySQL ERROR]', err);
            res.JSON('Registraion error ', err);
        });

        if (result && result.length) {
            res.end(JSON.stringify(result)); //if password true, return all info on user

        } else {
            res.json('User does not exist!');
        }
    });
}

//get_consultation_doctor
const get_consultation_doctor = (req, res) => {
    var post_data = req.body;

    //extract email and password from request
    var email = post_data.emailPatient;

    pool.query('SELECT * FROM Consultation where emailDoctor=?', [email], function(err, result) {
        pool.on('error', function(err) {
            console.log('[MySQL ERROR]', err);
            res.JSON('Registraion error ', err);
        });

        if (result && result.length) {
            res.end(JSON.stringify(result)); //if password true, return all info on user

        } else {
            res.json('User does not exist!');
        }
    });
}


//relationship
const relationship = (req, res) => {
    var post_data = req.body;

    //extract email and password from request
    var email = post_data.emailPatient;

    pool.query('SELECT * FROM Relationship WHERE emailPatient=?', [email], function(err, result) {
        pool.on('error', function(err) {
            console.log('[MySQL ERROR]', err);
            res.JSON('Registraion error ', err);
        });

        if (result && result.length) {
            res.end(JSON.stringify(result)); //if password true, return all info on user

        } else {
            res.json('User does not exist!');
        }
    });
}

//get_patient 
const get_patient = (req, res) => {
    var post_data = req.body;

    //extract email and password from request
    var email = post_data.emailPatient;

    pool.query('SELECT * FROM Patient where email=?', [email], function(err, result) {
        pool.on('error', function(err) {
            console.log('[MySQL ERROR]', err);
            res.JSON('Registraion error ', err);
        });

        console.log(JSON.stringify(result)); //if password true, return all info on user

        if (result && result.length) {
            res.end(JSON.stringify(result)); //if password true, return all info on user

        } else {
            res.json('User does not exist!');
        }
    });
}


//get_uri 
const get_uri = (req, res) => {
    var post_data = req.body;

    //extract email and password from request
    var email = post_data.emailPatient;
    var uri = post_data.uri;


    pool.query('UPDATE uri SET uri=? where emailPatient=?', [uri, email], function(err, result) {
        pool.on('error', function(err) {
            console.log('[MySQL ERROR]', err);
            res.JSON('Registraion error ', err);
        });
    });
}


//get_uri_for_profile 
const get_uri_for_profile = (req, res) => {
    var post_data = req.body;

    //extract email and password from request
    var email = post_data.emailPatient;

    pool.query('SELECT * FROM uri where emailPatient=?', [email], function(err, result) {
        pool.on('error', function(err) {
            console.log('[MySQL ERROR]', err);
            res.JSON('Registraion error ', err);
        });

        if (result && result.length) {
            res.end(JSON.stringify(result)); //if password true, return all info on user

        } else {
            res.json('User does not exist!');
        }
    });
}

//update_patient 
const update_patient = (req, res) => {
    var post_data = req.body;
    const firstName = post_data.firstName;
    const lastName = post_data.lastName;
    const email = post_data.email;
    const cin = post_data.CIN;
    const maritalStatus = post_data.maritalStatus;
    const birthDate =post_data.birthDate


    pool.query('SELECT * FROM Patient where email=?', [email], function(err, result) {
        pool.on('error', function(err) {
            console.log('[MySQL ERROR]', err);
            res.JSON('Registraion error ', err);
        });

        if (result && result.length) {

            pool.query('UPDATE Patient SET firstName=?', [firstName], function(err, result) {
                pool.on('error', function(err) {
                    console.log('[MySQL ERROR]', err);
                    res.JSON('Registraion error ', err);
                });
            });

            pool.query('UPDATE Patient SET lastName=?', [lastName], function(err, result) {
                pool.on('error', function(err) {
                    console.log('[MySQL ERROR]', err);
                    res.JSON('Registraion error ', err);
                });
            });

            pool.query('UPDATE Patient SET CIN=?', [cin], function(err, result) {
                pool.on('error', function(err) {
                    console.log('[MySQL ERROR]', err);
                    res.JSON('Registraion error ', err);
                });
            });

            pool.query('UPDATE Patient SET birthDate=?', [birthDate], function(err, result) {
                pool.on('error', function(err) {
                    console.log('[MySQL ERROR]', err);
                    res.JSON('Registraion error ', err);
                });
            });

            pool.query('UPDATE Patient SET maritalStatus=?', [maritalStatus], function(err, result) {
                pool.on('error', function(err) {
                    console.log('[MySQL ERROR]', err);
                    res.JSON('Registraion error ', err);
                });
            });
        } else {
            res.json('User does not exist!');
        }
    });
} 



module.exports = {
    register_doctor,
    register_patient,
    login,
    search,
    reset_password,
    get_appointment,
    get_doctor,
    get__all_doctors,
    add_appointment,
    get_hospitalisation,
    get_consultation,
    get_consultation_doctor,
    relationship,
    get_patient,
    get_uri,
    get_uri_for_profile,
    update_patient
};