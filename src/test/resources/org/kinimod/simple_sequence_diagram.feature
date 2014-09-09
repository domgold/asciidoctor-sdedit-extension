# language: en
Feature: sdedit diagram block

  Scenario: Simple sdedit diagram block
    Given the following asciidoctor content
      """
      = Title
      :outdir: <outdir>
      
      A simple sequence diagram rendered as png.
      
      [sdedit,node-1,png]
      ----
      bfs:BFS[a]
      /queue:FIFO
      
      bfs:queue.new
      bfs:queue.destroy()      
      ----
      """
    When I register the SdEditBlockProcessor
    And I render the asciidoctor content to html
    Then the file "in.html" exists
    And the file "node-1.png" exists
    And the file "in.html" contains the text "<img" and "node-1.png"

  Scenario: Complex sdedit diagram block
    Given the following asciidoctor content
      """
      = Title
      :outdir: <outdir>
      
      |===
      |hello |a
      
      |a | a
      |===
      
      == Simple Diagram
      
      A simple sequence diagram rendered as svg.
      
      [sdedit,node-1,svg]
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
    Then the file "in.html" exists
    And the file "node-1.svg" exists
    And the file "in.html" contains the text "<img" and "svg"
