#! /bin/sh

if [ -z "$JAVA_HOME" ]; then
    echo "You must set your JAVA_HOME environment variable. Do you have Java installed?"
    exit 1
fi

if [ ! -e "target/PGPSigner.jar" ]; then
   echo "please run 'ant jar' first!"
   exit 1
fi

java -jar target/PGPSigner-1.0.jar "$@"
