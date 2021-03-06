# ! /bin/sh

# Copyright (C) 2007 Henning P. Schmiedehausen
#
# See the NOTICE file distributed with this work for additional
# information
#
# Licensed under the Apache License, Version 2.0 (the "License"); you
# may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
# implied. See the License for the specific language governing
# permissions and limitations under the License.
#

APPNAME="PGPSigner"
APPVERSION="1.0"

if [ -z "$JAVA_HOME" ]; then
    echo "You must set your JAVA_HOME environment variable. Do you have Java installed?"
    exit 1
fi

if [ ! -e "target/${APPNAME}-${APPVERSION}.jar" ]; then
   echo "please run 'ant jar' first!"
   exit 1
fi

java -Xmx512m -Xms128m -jar target/${APPNAME}-${APPVERSION}.jar "$@"
