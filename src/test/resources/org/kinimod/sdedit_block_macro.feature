# language: en
Feature: Sdedit block macro
  The sdedit macro block processor allows to process files with sdedit content and output an image.

  Scenario Outline: Using the macro block processor
    Given the following asciidoctor content
      """
      = Title
      
      A simple sequence diagram rendered as <type>.
      
      sdedit::sequence.sd[type=<type>,returnArrowVisible=false]
      
      End.
      """
    And a file named "sequence.sd" with the following content
      """
       bfs:BFS[a]
       /queue:FIFO
       
       bfs:queue.new
       bfs:queue.destroy()      
      """
    When I register the SdEditBlockMacroProcessor
    And I render the asciidoctor content to html
    Then the rendered file contains the following text snippets:
      | <img            |
      | sequence.<type> |
    And the file "sequence.<type>" exists in the output directory.

    Examples: 
      | type |
      | png  |
      | svg  |

  Scenario: Try to reference a file that does not exist.
    Given the following asciidoctor content
      """
      = Title
      
      A simple sequence diagram rendered as <type>.
      
      sdedit::doesnotexist.sd[]
      
      End.
      """
    When I register the SdEditBlockMacroProcessor
    And I render the asciidoctor content to html
    Then the rendered file contains the following text snippets:
      | <img      |
      | error.png |

  Scenario: File with syntax error
    Given the following asciidoctor content
      """
      = Title
      
      A simple sequence diagram rendered as <type>.
      
      sdedit::sequence.sd[]
      
      End.
      """
    And a file named "sequence.sd" with the following content
      """
       bfs:BFS[a]
       /queue:FIFO
       
       bfsqueue.new
      """
    When I register the SdEditBlockMacroProcessor
    And I render the asciidoctor content to html
    Then the rendered file contains the following text snippets:
      | <img      |
      | error.png |
