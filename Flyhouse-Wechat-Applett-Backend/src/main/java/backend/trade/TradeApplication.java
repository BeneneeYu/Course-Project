package backend.trade;

import backend.trade.config.AppProperties;
import backend.trade.domain.Goods;
import backend.trade.domain.GoodsAnswer;
import backend.trade.domain.Talk;
import backend.trade.domain.TalkAnswer;
import backend.trade.repository.TalkAnswerRepository;
import backend.trade.repository.TalkRepository;
import backend.trade.repository.goods.GoodsAnswerRepository;
import backend.trade.repository.goods.GoodsRepository;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import java.util.HashSet;


@SpringBootApplication
@EnableCaching
@EnableConfigurationProperties({
        AppProperties.class
})
public class TradeApplication {

    public static void main(String[] args) {
        SpringApplication.run(TradeApplication.class, args);
    }
    /**
     * http重定向到https
     * @return
     */
    @Bean
    public TomcatServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint constraint = new SecurityConstraint();
                constraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                constraint.addCollection(collection);
                context.addConstraint(constraint);
            }
        };
        tomcat.addAdditionalTomcatConnectors(httpConnector());
        return tomcat;
    }

    @Bean
    public Connector httpConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        //Connector监听的http的端口号
        connector.setPort(8082);
        connector.setSecure(false);
        //监听到http的端口号后转向到的https的端口号
        connector.setRedirectPort(8443);
        return connector;
    }

    @Autowired
    GoodsAnswerRepository goodsAnswerRepository;

    @Autowired
    GoodsRepository goodsRepository;

    @Autowired
    TalkAnswerRepository talkAnswerRepository;

    @Autowired
    TalkRepository talkRepository;



    @Bean
    public CommandLineRunner dataLoader(GoodsAnswerRepository goodsAnswerRepository,GoodsRepository goodsRepository,TalkAnswerRepository talkAnswerRepository,TalkRepository talkRepository) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                for (GoodsAnswer goodsAnswer : goodsAnswerRepository.findAll()) {
                    if (null == goodsAnswer.getAnswerPublisher() || "null".equals(goodsAnswer.getAnswerPublisher())){
                        goodsAnswerRepository.delete(goodsAnswer);
                    }
                }
                for (Goods goods : goodsRepository.findAll()) {
                    if (null == goods.getUserName() || "null".equals(goods.getUserName())){
                        goodsRepository.delete(goods);
                        continue;
                    }
                    if(goods.getGoodIntroduction().contains("哈哈哈")){
                        goodsRepository.delete(goods);
                    }
                }
                for (TalkAnswer talkAnswer  : talkAnswerRepository.findAll()) {
                    if (null == talkAnswer.getAnswerPublisher() || talkAnswer.getAnswerContent().contains("6") || talkAnswer.getAnswerContent().contains("哈") || "null".equals(talkAnswer.getAnswerPublisher())){
                        talkAnswerRepository.delete(talkAnswer);
                    }
                }
                for (Talk talk : talkRepository.findAll()) {
                    if (null == talk.getTalkPublisher() || "null".equals(talk.getTalkPublisher())){
                        talkRepository.delete(talk);
                    }
                }
            }

        };
    }
}
