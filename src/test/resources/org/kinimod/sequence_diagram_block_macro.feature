# language: en
Feature: sdedit diagram block macro

  Background: 
    Given a simple message Ab
    And a second message
    And a table of entries
      | role |
      | url  |
      | data |

  Scenario: Simple sdedit diagram block
    Given the following asciidoctor content
      """
      = Title
      :outdir: <outdir>
      
      A simple sequence diagram rendered as png.
      
      sdedit::sequence.sd[type="png"]
      
      End.
      """
    And the sdedit file named "sequence.ad" with the following content
      """
       bfs:BFS[a]
       /queue:FIFO
       
       bfs:queue.new
       bfs:queue.destroy()      
      """
    When I register the SdEditBlockMacroProcessor
    And I render the asciidoctor content to html
    Then the file "in.html" exists
    And the file "node-1.png" exists
    And the file "in.html" contains the text "<img" and "node-1.png"

  Scenario: Complex sdedit diagram block
    Given the following asciidoctor content
      """
      = Title
      :outdir: <outdir>
      
      == Simple Diagram
      
      A simple sequence diagram rendered as svg.
      
      sdedit::sequence2.sd[type="png", format="Landscape"]

      """
    And the sdedit file named "sequence2.ad" with the following content
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
    Then the file "in.html" exists
    And the file "node-1.svg" exists
    And the file "in.html" contains the text "<img" and "svg"
