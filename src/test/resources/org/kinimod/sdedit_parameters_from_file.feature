# language: en
Feature: SdEdit parameters from file.
  As an asciidoc writer
  I want to pass in configuration parameters using an sdedit configuration file.

  Scenario Outline: Use a simple configuration file.
    Given the following asciidoctor content
      """
      = Title
            
      [sdedit,outputfilename=filename,type=svg<conf_attribute>]
      ----
      bfs:BFS[a]
      /queue:FIFO
      
      bfs:queue.new
      ----
           
      """
    And an sdedit config file named "<config_file>" <enabling_or_disabling> return arrows is in the same folder as the source document
    When I register the SdEditBlockProcessor
    And I render the asciidoctor content to html
    Then the rendered file contains the following text snippets:
      | <img         |
      | filename.svg |
    And the file "filename.svg" contains <num_arrows> return arrows.

    Examples: without attribute => from docdir with default name
      | enabling_or_disabling | num_arrows | config_file | conf_attribute |
      | enabling              | 1          | sdedit.conf |                |
      | disabling             | 0          | sdedit.conf |                |

    Examples: with attribute => from docdir with provided name
      | enabling_or_disabling | num_arrows | config_file | conf_attribute      |
      | enabling              | 1          | my.conf     | ,sdeditconf=my.conf |
      | disabling             | 0          | my.conf     | ,sdeditconf=my.conf |
