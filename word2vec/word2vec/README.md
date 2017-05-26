# word2vec
Exemplo utilizando o Word2Vec

Adicione este código ao pom.xml do seu projeto maven

```java 

 <properties>
        <nd4j.backend>nd4j-native-platform</nd4j.backend>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <shadedClassifier>bin</shadedClassifier>
        <java.version>1.7</java.version>
        <nd4j.version>0.6.0</nd4j.version>
        <dl4j.version>0.6.0</dl4j.version>
        <datavec.version>0.6.0</datavec.version>
        <arbiter.version>0.6.0</arbiter.version>
        <guava.version>19.0</guava.version>
        <jfreechart.version>1.0.13</jfreechart.version>
        <maven-shade-plugin.version>2.4.3</maven-shade-plugin.version>
        <exec-maven-plugin.version>1.4.0</exec-maven-plugin.version>
</properties>

<dependencies>
  <dependency>
     <groupId>org.deeplearning4j</groupId>
     <artifactId>deeplearning4j-ui</artifactId>
     <version>${dl4j.version}</version>
   </dependency>
   <dependency>
     <groupId>org.deeplearning4j</groupId>
     <artifactId>deeplearning4j-nlp</artifactId>
     <version>${dl4j.version}</version>
   </dependency>
   <dependency>
     <groupId>org.nd4j</groupId>
     <artifactId>nd4j-native</artifactId> 
     <version>${nd4j.version}</version>
   </dependency>
</dependencies>


```
#Treinando um modelo

Para treinar um modelo usando word2vec você precisa apenas de uma classe Java com os métodos a seguir:

```java

SentenceIterator iter = new LineSentenceIterator(new File("politica.txt"));
        iter.setPreProcessor(new SentencePreProcessor() {
            public String preProcess(String sentence) {
                return sentence.toLowerCase();
            }
        });
        
        
        TokenizerFactory t = new DefaultTokenizerFactory();
        t.setTokenPreProcessor(new CommonPreprocessor());
        
        
        Word2Vec vec = new Word2Vec.Builder()
                .minWordFrequency(1)
                .iterations(1)
                .layerSize(100)
                .seed(42)
                .windowSize(10)
                .iterate(iter)
                .tokenizerFactory(t)
                .build();

        vec.fit();
        
        //SALVANDO MODELO TREINADO
        WordVectorSerializer.writeFullModel(vec, "pathToSaveModelPolitica.txt");
 
  ```
       
Neste caso o arquivo utilizado para treinamento será o arquivo "politica.xml"
        
#Lendo um modelo treinado
        
Depois que você treinar um modelo, você irá salvá-lo para poder utilizar novamente.
No código acima salvamos o modelo utilizando o método wirteFullModel como a seguir:
        
        
  ```java
        
        WordVectorSerializer.writeFullModel(vec, "pathToSaveModelPolitica.txt");
        
  ```
        
Para ler o modelo salvo, devemos utilizar o método WordVectorSerializer.loadFullModel da seguinte forma:
        
        
 ```java
 
 
        //LENDO MODELO TREINADO
		    Word2Vec vec = WordVectorSerializer.loadFullModel("pathToSaveModelPolitica.txt");
        
        Collection<String> lst = vec.wordsNearest("temer", 10);
        System.out.println(lst);       
               
        double cosSim = vec.similarity("temer", "presidente");
        System.out.println(cosSim);	
        
        
  ```
        
        
O método  vec.wordsNearest("temer", 10) calcula as 10 palavras mais próximas da palavra "temer".
O método vec.similarity("temer", "presidente"); calcula a similaridade do cosseno entre as palavras "temer" e "presidente"
        
        
Maiores detalhes no site: <https://deeplearning4j.org/word2vec>
