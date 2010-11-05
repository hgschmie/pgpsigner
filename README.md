PGPSigner
=========

This program helps you to manage, sign and send out PGP keys after a
PGP/GPG key signing event.


Copyright and License
=====================

PGPSigner is Copyright (C) 2007 Henning P. Schmiedehausen. It is
distributed under the Apache Software License 2.0.

This program contains a number of third-party licenses. Please review
the NOTICE file for their licenses.


Contact
=======

If you have questions, patches or anything else concerning the
PGPSigner application, please contact me through the github issue
tracker.

If this program spawns enough interest, I might set up a mailing list
for it.


Strong cryptography
===================

This application uses strong cryptography, something that might pose
problems for you if you happen to live in a region of the world where
this is an issue.

To use this application, you must probably install the "Java
Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy
Files" for the Sun JCE. If you encounter the following error:

`java.lang.SecurityException: Unsupported keysize or algorithm parameters,`

then this is most likely the problem. Download these for the Sun JDK
1.5 at <http://java.sun.com/javase/downloads/index_jdk5.jsp> (scroll
down to the bottom of the page). 


Requirements and Download
=========================

This application is written in Java and requires at least Java 1.5 to
run. 

It is currently only available in source form from <http://github.com/hgschmie/pgpsigner>
repository or as a distribution archive. You need a JDK 1.5 or better
and Apache ant 1.6 or better to build this application.

* <http://www.softwareforge.de/releases/pgpsigner/PGPSigner-1.0-src.tar.gz>
* <http://www.softwareforge.de/releases/pgpsigner/PGPSigner-1.0-src.zip>

Release History
===============

2007-05-20 - Released Version 1.0


Notes
=====

Binary distribution
-------------------

For a binary distribution, I'd like to have a consistent way to
package an interactive application that uses JCE in a single-file. I
tried one-jar (http://one-jar.sourceforge.net/) and it chokes on the
JCE security provider. If you can bind this application so that the
"unlock" command still works, I'm more than happy to accept patches
and will offer a binary distribution. Until then, you must download
the source distribution and run the included shell script.

If you use the Windows platform and have launch4j and/or JSmooth based
patches to build working Windows executables, I will add them happily.


Local files
-----------

PGPSigner *never* modifies any of the files that it reads. So you can
redo all the signing operations as often as you want to without
changing your local key rings. It also does not store any state or
information on your local disk, so if you quit the application, all
state is lost. This is intentional because it makes sure that
sensitive information such as your unlocked private keys are never
stored anywhere except in memory.


Execution
=========

PGPSigner includes a shell script to run it. Please run 

`./pgpsigner.sh <options>`

to execute the application. You must compile the application first by 
executing "ant jar" from the root directory.


Usage
=====

Signing PGP keys at a key signing party is normally done using a
paper-and-pencil approach. The well known Key signing party HOWTO
(<http://cryptnet.net/fdp/crypto/keysigning_party/en/keysigning_party.html>)
shows you how to sign keys manually using gpg, which is cumbersome and
error prone if you have lots of keys to sign.

This application allows you to sign, mail and upload keys
interactively.

The best case scenario is, that the public keyring for your keysigning
party is available for download. If it is not available, you need to
download the public keys on the party key ring manually before running
the PGPSigner application.

After starting the program, it will show the user prompt 

`PGPSigner>`

You can request a list of the available commands by entering
"help". Pressing the <TAB> key shows all commands and offers command
line completion.

To sign a party key ring, you must load your private and public key
rings and also the party key ring. You must also select one of your
private keys as the signing key.

When loading the key rings, PGPSigner will automatically check which
keys have already been signed with the selected signing key and remove
these keys.

You then review the list of keys using the "list" command and remove
all keys for which you could not verify the identity of the key owner
at the key signing party with the "remove" command.

Now you should have a list of keys that you want to sign. First you
must unlock your private key by running the "unlock" command and
entering your secret pass phrase. Then you can run the "sign" command
to sign all the keys in the key list.

It is important to understand that by signing these keys, you have not
yet added the signatures to your own public key ring. PGPSigner
*never* modifies any of the key rings that have been loaded. So you
can redo all the signing operations as often as you want to without
changing your key rings.

The signed keys can be published in two ways: First by mailing the
signed keys to their owners (and to yourself) so they can be added to
public key rings. Secondly you can upload the signed keys to a public
key server. Some people consider uploading foreign keys to key servers
impolite, so it is your choice to do so or not.

For mailing the keys out, it is necessary to set the name of the
sigining party event (e.g. "ApacheCon Europe 2007") using the
"signevent" command, so that the recipients know where they met you
and where you verified their id. Of course it is also necessary to set
the host name of a mail server that accepts SMTP email from you using
the "mailserver" command. Mail addresses get extracted from the uid
fields inside the keys, so it is necessary that the uid field contains
them in angle ("<" and ">") brackets.

The "mail" command does the actual mailing. Your own mail address (the
one present in the signing key) will get a copy of the mails. You can
then import these mails to add the signatures to your own key ring.

Uploading keys to a keyserver is a similar straightforward
process. You set the keyserver host name you want to upload the key to
with the "keyserver" command. The keyserver must support the widely
accepted "HKP" protocol and listen on port 11371. Most popular
keyserver support this. Once you have set the keyserver host name, you
upload your signed keys with the "upload" command.


Command reference
=================

Some of these commands are also available from the command line. You
can then add "-<option> [parameter]" to the command line. 

E.g. the "partyring" command is also available as 

-partyring <partyring file name>

or the "help" command as 

-help

By adding options to the command line, you do not have to type them in
interactive mode all the time.

Commands that can be used from the command line are marked with a (*).


help (*) - display help
-----------------------

Displays a short list of all commands. The command line help option also
terminates the program.


quit - quit PGPSigner
---------------------

Leaves the PGPSigner application.


reset - reset all application state
-----------------------------------

Clears all set parameters, the key ring files and all settings.


show - show the current options
-------------------------------

Displays the current application state, including the key ring files,
server settings and which key is used for signing.


list - list the keys available for signing
------------------------------------------

If a party key ring has been loaded, it displays all the keys that are
eligible for signing. The flags shown in the second column are as follows:

* S - The key has been signed with the current sign key
* U - The key has been uploaded to the current key server
* M - The key was mailed to its owner


unlock - unlock the current sign key
------------------------------------

If the current signing key is not yet unlocked, ask for the secret
pass phrase and try to unlock the key.


simulate (*) - toggle simulation flag
-------------------------------------

The simulation flag changes the behaviour of the mail and upload
commands. The mail command sends the signed keys just to the mail
address recorded in the signing key (and not to the key owners) and
the upload command just simulates the upload but does not connect to
the key server. 


partyring (*) - sets the party key ring - Filename completion
-------------------------------------------------------------

This command selects the key ring file containing the keys to sign.

This command offers file name completion with the <TAB> key.


publicring (*) - sets the public key ring - Filename completion
---------------------------------------------------------------

This command selects the public key ring which is used to verify which
keys on the party ring have already signed with the sign key.

The public key ring which can be used by PGPSigner is an Ascii file.

If you are using gpg2, the command to create the file would be

gpg2 --export -a "your name">  public.key

where of course you replace "your name" by the name entered with the keys.


This command offers file name completion with the <TAB> key.


secretring (*) - sets the secret key ring - Filename completion
---------------------------------------------------------------

This command selects the secret key ring containing one or more keys
available for key signing.

This command offers file name completion with the <TAB> key.

The private key ring which can be used by PGPSigner is an Ascii file.

If you are using gpg2, the command to create the file would be

gpg2 --export-secret-key -a "your name">  private.key

where of course you replace "your name" by the name entered with the keys.


signkey (*) - sets the signing key - Secret key id completion
-------------------------------------------------------------

The "signkey" command selects a key from your secret key ring for
signing. Selecting a sign key also checks the keys on the party ring
whether they already have been signed with this key. If yes, it
removes these keys from the party ring.

This command offers <TAB> completion on the available secret key ids.


mailserver (*) - sets the mailserver for mailing keys
-----------------------------------------------------

Set a mail server host name for mailing signed keys out. This server
must accept SMTP connections without authentication from the sender
address recorded in the sign key. Usually this is a local SMTP server
or the server of your ISP. Changing the mail server also resets the
"mailed out" flag on all keys in the party key ring.


keyserver (*) - sets the keyserver for uploading keys
-----------------------------------------------------

Sets the key server host name for uploading keys. This server must
accept connections on port 11371 using HKP (Horowitz Key
Protocol). Setting the key server resets the "is uploaded" flag on all
signed keys.


signevent (*) - sets the key signing event name
-----------------------------------------------

Sets the name of the sign event where you verified key ids. This is
required for mailing out signed keys to their owners.


remove - remove a key from the party list - Party key id completion
-------------------------------------------------------------------

Removes a key from the party key ring. Usually you remove all keys
where you could not verify the owner's id before signing all keys on
the party key ring.

This command offers <TAB> completion on all key ids still on the party
key ring.


write - write an ASCII-armored key from the party list - Party key id completion
--------------------------------------------------------------------------------

Write a key on the party key ring to the console using ASCII armored
output. This is suitable for cut-and-paste into a PGP key importing
application.

This command offers <TAB> completion on all key ids on the party key
ring.


sign - sign keys on the party key ring
--------------------------------------

Sign all keys on the party key ring. To run this command, you must
select a key from the secret key ring to sign and unlock it first.


mail - mail signed keys to their owners
---------------------------------------

Mail all signed keys on the party key ring to their owners. The mail
address in the sign key will also get a copy of each mail, which can
be imported into your public key ring. If this command is run in
simulation mode, then only the mail address in the sign key will get a
mail message.

This command requires a selected sign key and it must contain a mail
address in angle brackets ("<" and ">") in its uid field. If a sign
key has no mail address, it can not be used to mail out signed keys.

This command sets the "mailed out" flag on all keys that have been
successfully sent out.


upload - upload signed keys to a public key server
--------------------------------------------------

Upload all signed keys on the party key ring to a public key server. 

This command sets the "uploaded" flag on all keys that have been
successfully sent out.
