# A simple Java 3D drawer

![RotatableCube](/readme-resources/showcase.gif)

## Tricks

To get a point's corresponding location on screen:
1. Calculate the projection point on the screen.
2. Use an inverse ration to shrink the distance between screen center and the point. 
this meant to simulate the real world.

To correctly block and being blocked:
1. Get all the points (as many as possible) in all triangles. The more the points obtained, the higher the quality. 
2. Sort these points by `z` (the depth).
3. Draw each of these points from far to near.