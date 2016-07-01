Yaio - MetaExtract-Service
=====================

# Desc
A webservice to extrct metadata from uploaded documents or urls.

# Build and run
- test it

        mvn install
        java -Xmx768m -Xms128m -Dspring.config.location=file:config/metaextract-application.properties -Dlog4j.configuration=file:config/log4j.properties -cp "dist/yaio-metaextract-service-full.jar" de.yaio.services.metaextract.server.MetaExtractApplication --config config/metaextract-application.properties
        curl --user metaextract:secret -X POST http://localhost:8082/services/metaextract/getByUrl -F 'url=http://www.google.de' -F 'lang=de'
        curl --user metaextract:secret -X POST http://localhost:8082/services/metaextract/getByFile -F 'file=@/cygdrive/d/tmp/readme.txt' -F 'lang=de'

# Thanks to
- **Build-Tools**
    - [Apache Maven](https://github.com/apache/maven)
    - [Eclipse](http://eclipse.org/)
- **Java-Core-Frameworks**
    - [Spring-Framework](https://github.com/spring-projects/spring-framework)
    - [Spring-boot](https://github.com/spring-projects/spring-boot)
    - [Spring Security](https://github.com/spring-projects/spring-security)
- **Data Parser**
    - [Apache TiKa](https://github.com/apache/tika/)
    - [Tesseract](https://github.com/tesseract-ocr/tesseract)
    - [Tess4J](http://tess4j.sourceforge.net/)

# License
    /**
     * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
     * @category collaboration
     * @copyright Copyright (c) 2010-2014, Michael Schreiner
     * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
     *
     * This Source Code Form is subject to the terms of the Mozilla Public
     * License, v. 2.0. If a copy of the MPL was not distributed with this
     * file, You can obtain one at http://mozilla.org/MPL/2.0/.
     */
