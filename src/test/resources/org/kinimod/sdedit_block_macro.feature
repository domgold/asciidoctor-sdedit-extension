# language: en
Feature: sdedit block macro
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

  Scenario: Using the macro block processor with a complex sdedit diagram block
    Given the following asciidoctor content
      """
      = Title
      
      == Simple Diagram
      
      A simple sequence diagram rendered as svg.
      
      sdedit::sequence2.sd[type="png", format="Landscape"]

      """
    And the file named "sequence2.sd" with the following content
      """
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
      """
    When I register the SdEditBlockMacroProcessor
    And I render the asciidoctor content to html
    Then the rendered file contains the following text snippets:
      | <img         |
      | sequence.png |
    And the file "sequence.png" exists in the output directory.
