# language: en
Feature: sdedit block
  As an asciidoc write
  I want to pass in configuration parameters using an sdedit configuration file.

  Scenario Outline: Use sdedit configuration parameters
    Given the following asciidoctor content
      """
      = Title
      
      Sequence diagram rendered with all default configuration parameters passed in.
      
      The following source :
      
      ....
      [sdedit,<parameter_name>=<parameter_value>,outputfilename=<parameter_name>]
      ----
      bfs:BFS[a]
      /queue:FIFO
      
      bfs:queue.new
      bfs:queue.destroy()      
      ----
      ....
           
      [sdedit,<parameter_name>=<parameter_value>,outputfilename=<parameter_name>]
      ----
      bfs:BFS[a]
      /queue:FIFO
      
      bfs:queue.new
      bfs:queue.destroy()      
      ----
      """
    When I register the SdEditBlockProcessor
    And I render the asciidoctor content to html
    Then the rendered file contains the following text snippets:
      | <img                 |
      | <parameter_name>.png |
    And the file "<parameter_name>.png" exists in the output directory.

    Examples: 
      | parameter_name             | parameter_value |
      | actorWidth                 | 25              |
      | allowMessageProperties     | false           |
      | arrowSize                  | 6               |
      | colorizeThreads            | true            |
      | fragmentMargin             | 8               |
      | fragmentPadding            | 10              |
      | fragmentTextPadding        | 3               |
      | noteMargin                 | 6               |
      | notePadding                | 6               |
      | destructorWidth            | 30              |
      | glue                       | 10              |
      | headHeight                 | 35              |
      | headLabelPadding           | 5               |
      | headWidth                  | 100             |
      | initialSpace               | 10              |
      | leftMargin                 | 5               |
      | lineWrap                   | false           |
      | lowerMargin                | 5               |
      | mainLifelineWidth          | 8               |
      | messageLabelSpace          | 3               |
      | messagePadding             | 6               |
      | opaqueMessageText          | false           |
      | returnArrowVisible         | true            |
      | rightMargin                | 5               |
      | selfMessageHorizontalSpace | 15              |
      | separatorBottomMargin      | 8               |
      | separatorTopMargin         | 15              |
      | shouldShadowParticipants   | true            |
      | spaceBeforeActivation      | 2               |
      | spaceBeforeAnswerToSelf    | 10              |
      | spaceBeforeConstruction    | 6               |
      | spaceBeforeSelfMessage     | 7               |
      | subLifelineWidth           | 6               |
      | threadNumbersVisible       | false           |
      | threaded                   | true            |
      | upperMargin                | 5               |
      | verticallySplit            | true            |

  Scenario: Use all sdedit configuration parameters
    Given the following asciidoctor content
      """
      = Title
      
      Sequence diagram rendered with all default configuration parameters passed in.
      
      The following source :
      
      ....
      [sdedit,actorWidth=25,allowMessageProperties=false,arrowSize=6,colorizeThreads=true,fragmentMargin=8,fragmentPadding=10,fragmentTextPadding=3,noteMargin=6,notePadding=6,destructorWidth=30,glue=10,headHeight=35,headLabelPadding=5,headWidth=100,initialSpace=10,leftMargin=5,lineWrap=false,lowerMargin=5,mainLifelineWidth=8,messageLabelSpace=3,messagePadding=6,opaqueMessageText=false,returnArrowVisible=true,rightMargin=5,selfMessageHorizontalSpace=15,separatorBottomMargin=8,separatorTopMargin=15,shouldShadowParticipants=true,spaceBeforeActivation=2,spaceBeforeAnswerToSelf=10,spaceBeforeConstruction=6,spaceBeforeSelfMessage=7,subLifelineWidth=6,threadNumbersVisible=false,threaded=true,upperMargin=5,verticallySplit=true,outputfilename=test]
      ----
      bfs:BFS[a]
      /queue:FIFO
      
      bfs:queue.new
      bfs:queue.destroy()      
      ----
      ....
           
      [sdedit,actorWidth=25,allowMessageProperties=false,arrowSize=6,colorizeThreads=true,fragmentMargin=8,fragmentPadding=10,fragmentTextPadding=3,noteMargin=6,notePadding=6,destructorWidth=30,glue=10,headHeight=35,headLabelPadding=5,headWidth=100,initialSpace=10,leftMargin=5,lineWrap=false,lowerMargin=5,mainLifelineWidth=8,messageLabelSpace=3,messagePadding=6,opaqueMessageText=false,returnArrowVisible=true,rightMargin=5,selfMessageHorizontalSpace=15,separatorBottomMargin=8,separatorTopMargin=15,shouldShadowParticipants=true,spaceBeforeActivation=2,spaceBeforeAnswerToSelf=10,spaceBeforeConstruction=6,spaceBeforeSelfMessage=7,subLifelineWidth=6,threadNumbersVisible=false,threaded=true,upperMargin=5,verticallySplit=true,outputfilename=test]
      ----
      bfs:BFS[a]
      /queue:FIFO
      
      bfs:queue.new
      bfs:queue.destroy()      
      ----
      """
    When I register the SdEditBlockProcessor
    And I render the asciidoctor content to html
    Then the rendered file contains the following text snippets:
      | <img     |
      | test.png |
    And the file "test.png" exists in the output directory.
