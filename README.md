# CardManager

I've built a secure storage application where the user can create a database to save vital information which he can unlock using
his master password and his AES256 key. The app check the files for tempering by generating a checksum, hasing it and signing it using
the apps RSA key and then sings everything using the users key. 

##The proccess is for the integrity check is the following

(User Files) -> (SHA3_256) -> (User name, Digest) -> (SHA3_256) -> Bytes(O10101) -> Digital Sign (Apps Key RSA) -> Digital Sign(Users Key AES) -> Encrypted Signature


This project was built for the course of <Strong>Information and Communication Systems Security</Strong>. 
