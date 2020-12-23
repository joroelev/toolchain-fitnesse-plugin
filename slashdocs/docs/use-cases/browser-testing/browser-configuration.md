# Browser configuration

In order to test in your browser, a webdriver needs to be configured that starts and steers your web browser. This page describes
common ways to start a driver.

## Starting a local browser

Starting a browser is done using the [Selenium Driver Setup](/fixtures/hsac-fixtures/selenium-driver-setup/) fixture and 
can be as simple as calling 

`| start driver for | chrome/firefox/edge/safari |` 

from your script.

It's good practice to do this in your _SuiteSetUp_, so a browser is started once to run your tests vs. starting a new browser for each
test case.

However, in many cases, you may want to specify some more browser configuration. In selenium terms, you may want to provide
some _capabilities_ or _options_. For a local browser, this can be done by providing a hashmap containing key-value pairs.

Some examples:
??? note "Chrome with default config"
    ```
    |script          |selenium driver setup | 
    |start driver for|chrome                |
    ```      

??? note "Chrome Headless"
    ```
    |script   |map fixture                        |
    |set value|--headless             |for|args[0]|
    |set value|--disable-gpu          |for|args[1]|
    |set value|--window-size=1920,1080|for|args[2]|
    |$options= |copy map                          |
    
    |script          |selenium driver setup       |
    |start driver for|chrome|with profile|$options|
    ```  
    Note that for headless mode, we should provide the desired window size.
    This effectively fills a map with a key called 'args' that holds a list of 3 values and saves it to a slim symbol called _$options_
    The slim symbol containing ths map is the passed to the 'start driver with profile' method to start the preconfigured browser.
    
??? note "Edge Maximized"
    ```
    |script   |map fixture                |
    |set value|start-maximized|for|args[0]|
    |$options=|copy map                   |
    
    |script          |selenium driver setup     |
    |start driver for|edge|with profile|$options|
    ```  
    This effectively fills a map with a key called 'args' that holds a list of 1 value and saves it to a slim symbol called _$options_
    The slim symbol containing ths map is the passed to the 'start driver with profile' method to start the preconfigured browser.  
    
## Starting a remote browser

A local browser is not always ideal. In order to be able to test with different versions of browsers, or even combinations 
of browsers and operating systems, using a selenium grid is common practice. These grids can be configured on the local machine (i.e. using
Docker containers), somewhere in the local network or one can use browsers provided by cloud providers such as Browser Stack or SauceLabs.

To use a remote browser we always need:
1. An endpoint
2. A set of capabilities

Some exaples of connecting to remote browsers:

??? note "Basic Chrome on a local grid"
    ```
    |script              |selenium driver setup                                               |
    |connect to driver at|http://127.0.0.1:4444/wd/hub|with capabilities|!{browserName:chrome}|
    ```  
    Note that we have used the !{} markup to define a map in this case. 
    For more info on hash table markup, see <a href="http://docs.fitnesse.org/FitNesse.FullReferenceGuide.UserGuide.FitNesseWiki.MarkupLanguageReference.MarkupHashTable" target="_blank">FitNesse's User Guide</a>
    on this topic. The capabilities map can also be created using [Map Fixture](/fixtures/hsac-fixtures/map-fixture/), like in the Browserstack example
        

??? note "Specific browser/OS config on a local grid"
    ```
    |script              |selenium driver setup                                                                                            |
    |connect to driver at|http://grid_host:port/wd/hub|with capabilities|!{browserName:internet explorer, platform:Windows 8.1, version:11}|
    ```  
    Note that we have used the !{} markup to define a map in this case. 
    For more info on hash table markup, see <a href="http://docs.fitnesse.org/FitNesse.FullReferenceGuide.UserGuide.FitNesseWiki.MarkupLanguageReference.MarkupHashTable" target="_blank">FitNesse's User Guide</a>
    on this topic. The capabilities map can also be created using [Map Fixture](/fixtures/hsac-fixtures/map-fixture/), like in the Browserstack example
        
    
??? note "Specific browser on Browserstack"
    ```
    |script                                |map fixture                                  |
    |expand periods in names to nested maps|false                                        |
    |set value                             |USER_NAME  |for|browserstack.user            |
    |set value                             |API_KEY    |for|browserstack.key             |
    |set value                             |OS X       |for|os                           |
    |set value                             |Catalina   |for|os_version                   |
    |set value                             |Firefox    |for|browser                      |
    |set value                             |latest     |for|browser_version              |
    |set value                             |1920x1080  |for|resolution                   |
    |set value                             |My Project |for|project                      |
    |set value                             |My Test Run|for|name                         |
    |set value                             |false      |for|browserstack.local           |
    |set value                             |3.141.59   |for|browserstack.selenium_version|
    |$browserstackCapabilites=             |copy map                                     |
    
    |script              |selenium driver setup                                                               |
    |connect to driver at|https://hub-cloud.browserstack.com/wd/hub|with capabilities|$browserstackCapabilites|
    ```  
    More on <a href="https://www.browserstack.com/automate/capabilities" target="_blank">Browserstack Capabilities</a>.


??? note "Specific browser on Saucelabs"
    ```
    |script                 |map fixture                  |
    |set value              |SAUCE_USER|for|username      |
    |set value              |ACCESS_KEY|for|accessKey     |
    |set value              |Windows 7 |for|platformName  |
    |set value              |65.0      |for|browserVersion|
    |$saucelabsCapabilities=|copy map                     |
    
    |script              |selenium driver setup                                                                      |
    |connect to driver at|https://ondemand.eu-central-1.saucelabs.com/wd/hub|with capabilities|$saucelabsCapabilities|
    ```  
    More on <a href="https://wiki.saucelabs.com/display/DOCS/Platform+Configurator#/" target="_blank">Saucelabs Capabilities</a>.


## Overriding config from the commandline

Whenever a test is run [from the command line](/TODO:LINK_HERE) using the JUnit runner, it is possible to override the Selenium
configuration. This allows for testing with multiple browsers without the need to have different wiki pages for each run.

In order to override the browser's configuration, combinations of system properties can be used. The following options are valid:

 - `-DseleniumBrowser=chrome`  will start a local chrome browser, regardless of any setting in the wiki
 - `-DseleniumGridUrl=http://localhost:4444/wd/hub -DseleniumBrowser=chrome`  will request any chrome browser from a local Selenium grid hub
 - `-DseleniumGridUrl=http://localhost:4444/wd/hub -DseleniumCapabilities=browserName:chrome, version:64, platform=LINUX`  will request chrome 64 on Linux from a local Selenium grid hub.
 Note that the _seleniumCapabilities_ property only accepts single key/value pairs.
 - `-DseleniumGridUrl=http://localhost:4444/wd/hub -DseleniumJsonCapabilities={"browserName":"chrome", "args":["headless", "disable-gpu", "lang=en_EN"]}`  will request chrome in headless mode with English locale from a local Selenium grid hub.
 
The last combination is the most flexible, but can get complex:

??? note "Complex JSON Capabilities object"
    ```
    -DseleniumJsonCapabilities="{\"acceptInsecureCerts\": true,\"browserName\": \"chrome\",\"pageLoadStrategy\":\"none\",\"chromeOptions\":{\"args\":[\"window-size=1920,1080\"],\"prefs\":{\"download.default_directory\": \"/tmp/downloads\"}}}" 
    ```
    
## Stopping the browser

Stopping any browser is done by calling _'stop driver'_ from Selenium driver setup. This stops the driver and exits the browser.
Note that omitting this will keep browsers open and may eventually lead to orphaned webdriver processes, especially in Windows.

??? note "Stop browser example"
    ```
    |script|selenium driver setup|
    |show  |run summary          |
    |stop driver                 |
    ``` 

