# language: en
Feature: SdEdit block
  The sdedit block processor allows to process asciidoc blocks with sdedit content and output an image.

  Scenario: Using the block processor
    Given the following asciidoctor content
      """
      = Title
      
      A simple sequence diagram rendered as png.
      
      [sdedit]
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
      | <img       |
      | sdedit.png |
    And the file "sdedit.png" exists in the output directory.

  Scenario: Try to render content with syntax error
    Given the following asciidoctor content
      """
      = Title
      
      A simple sequence diagram with syntax error rendered as png.
      
      [sdedit]
      ----
      bfs:BFS[a]
      /queue:FIFO
      
      bfsqueue.new
      ----
      """
    When I register the SdEditBlockProcessor
    And I render the asciidoctor content to html
    Then the rendered file contains the following text snippets:
      | <img      |
      | error.png |

  Scenario: Using the block processor with a complex sdedit diagram block
    Given the following asciidoctor content
      """
      = Title
      
      == Simple Diagram
      
      A simple sequence diagram rendered as svg.
      
      [sdedit,foo,svg]
      ----
      bfs:BFS[a]
      /queue:FIFO
      someNode:Node
      node:Node
      adjList:List
      adj:Node
      
      bfs:queue.new
      bfs:someNode.setLevel(0)
      bfs:queue.insert(someNode)
      [c:loop while queue != ()]
       bfs:node=queue.remove()
       bfs:level=node.getLevel()
       bfs:adjList=node.getAdjacentNodes()
       node:node.test()
       [c:loop 0 <= i < #adjList]
        bfs:adj=adjList.get(i)
        bfs:nodeLevel=adj.getLevel()
        [c:alt nodeLevel IS NOT defined]
          bfs:adj.setLevel(level+1)
          bfs:queue.insert(adj)
          --[else]
          bfs:nothing to do
        [/c]
       [/c]
      [/c]
      bfs:queue.destroy()      
      ----
      """
    When I register the SdEditBlockProcessor
    And I render the asciidoctor content to html
    Then the rendered file contains the following text snippets:
      | <img    |
      | foo.svg |
    And the file "foo.svg" exists in the output directory.
