# Spring Boot Caching Example

We're going to create a web application that display the answer to life, the universe and everything. Since this is a rather complicated question, it takes a bit of time to figure out the answer - probably millions of years, but for this example we'll make it 10 seconds.

We'll start by making a Spring Boot web application that uses Freemarker, Actuator and Cache.

    $ spring init caching -d=web,freemarker,actuator,cache
    Using service at https://start.spring.io
    Initializr service call failed using 'https://start.spring.io' - service returned Bad Request: 'Dependency 'cache' is not compatible with Spring Boot 1.2.5.RELEASE'

At the time of this screencast, the release version of Spring Boot is 1.2.5 and caching support won't be available until 1.3.0 is released. Fortunately we can tell the Spring Boot CLI that we want to use a milestone version of 1.3.0.

    $ spring init caching -d=web,freemarker,actuator,cache -b=1.3.0.M5
    $ cd caching
    
## Controller and Service

Let's create a simple controller that uses a service to calculate the answer and display it in a Freemarker template.

    @Controller
    public class AnswerController {
        @Autowired
        AnswerService answerService;
    
        @RequestMapping("/")
        public String index(Model model) throws InterruptedException {
            model.addAttribute("answer", answerService.getAnswer());
    
            return "index";
        }
    }
 
And here is the service that provides the answer after a short wait:
   
    @Service
    public class AnswerService {
        public int getAnswer() throws InterruptedException {
            Thread.sleep(10000);
            return 42;
        }
    }
    
Finally, here is the template that we will use to display the answer in `src/main/resources/templates/index.ftl`:

    <html>
        <head>
            <title>The Answer!</title>
        </head>
        <body>
            <h1>The answer!</h1>
    
            <p>The answer to life, the universe and everything is: ${answer}</p>
        </body>
    </html>

Now we start up our web app:

    $ mvn spring-boot:run
    
And head to <http://localhost:8080/> for enlightenment after a 10 second wait.

## Basic Caching

Every time we load the page we are waiting 10 seconds for the service to do its "work".

To cache the answer once the work is done, we only need to add the `@Cacheable` annotation to our method and give it a name:

    @Service
    public class AnswerService {
        @Cacheable("answer")
        public int getAnswer() throws InterruptedException {
            Thread.sleep(10000);
            return 42;
        }
    }
 
Now if we restart the app and reload the page, the first time will take 10 seconds but any subsequent load will be almost instantaneous.

We can see the size of our cache by using the `/metrics` end points provided by [Spring Boot Actuator](http://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#production-ready). It will only ever be 1 since we're only caching one piece of information.

## Long Term Caching

Currently, any time we restart the app the cache will be cleared and the first request will once again take 10 seconds. To combat that, we'd like to use a caching solution that allows for long-term caching across restarts. [Redis](http://redis.io) can helps us out here.

On the Mac we can use [Homebrew](http://brew.sh) to install redis for us.

    $ brew install redis
    
Then we can open another terminal window and start it up:

    $ brew info redis
    $ redis-server /usr/local/etc/redis.conf
    
We then add `spring-boot-starter-redis` as a dependency to our project in our `pom.xml`:
 
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-redis</artifactId>
    </dependency>

Now we restart our application and request the page a couple of times to show it is cached.  Then, if we restart our application we can see that the first request no longer takes 10 seconds because it's stored in Redis.