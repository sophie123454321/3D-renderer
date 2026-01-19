# Software 3D Renderer (Java)

A custom software-based 3D renderer written entirely in Java, built from scratch without using external 3D or graphics libraries.
This project renders a mutable 3D world composed of cubes and implements its own camera system, projection math, and rendering pipeline.
Inspired by Minecraft.

---

## Features

* Custom 3D renderer written from the ground up
* Camera with position, yaw, and pitch
* Perspective projection from 3D space to 2D screen
* Cube-based world generation
* Face visibility and basic clipping logic
* Asset loading for textures and visuals

---

## Controls

* W/A/S/D - move laterally
* C - move down
* SPACE - move up
* MIDDLE MOUSE - toggle edit mode
* LEFT CLICK - remove cube
* RIGHT CLICK - add cube
* SCROLL WHEEL - cycle through avaiable cube types
* F - toggle hold-to-remove or hold-to-add
* O - load previous world
* P - save current world
* L - toggle mouse lock
* ESC - close

---

## Project Structure

3D RENDERER/

├── src/        
├── assets/     
├── saves/      
├── README.md

└── .gitignore

* src/ contains all rendering, math, and world logic
* assets/ stores textures and image resources
* saves/ stores save files created at runtime

---

## How It Works

1. A 3D world is generated from cube primitives
2. Each cube is defined by 8 vertices and 6 faces
3. Vertices are transformed relative to the camera position and rotation
4. 3D coordinates are projected into 2D screen space
5. Faces are rendered and clipped based on camera visibility and depth

All math and transformations are implemented manually without relying on OpenGL, JavaFX 3D, or similar libraries.

---

## How to Run

1. Clone or download the repository
2. Make sure Java (JDK 8 or newer) is installed
3. Compile the project
4. Run Launcher.java

---

## Save Data

Runtime-generated data (such as world or state data) is stored separately from source code.

* Save files are written to the `saves/` directory
* Save files are ignored by Git using `.gitignore`
* No user-specific runtime data is committed to the repository

---

## What I Learned

* 3D vector math and coordinate transformations
* Camera systems using yaw and pitch rotation
* Perspective projection and screen-space mapping
* Managing near-plane and visibility edge cases
* Structuring a non-trivial Java project
* Separating runtime data from source control

---

## Future Improvements

* More robust depth sorting than z-buffering
* Texture mapping and lighting
* Chunked world generation for performance

---

## License

This project is provided for educational and demonstration purposes.

