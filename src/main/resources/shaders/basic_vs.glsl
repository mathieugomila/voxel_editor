#version 330 core
layout (location = 0) in vec3 position; 
layout (location = 1) in vec3 normal; 
layout (location = 2) in vec3 color;
  
out vec3 normal_pass;
out vec3 position_pass;
out vec3 color_pass; 

uniform mat4 MVP;

void main()
{
    vec4 final_position = MVP * vec4(position, 1.0); 
    gl_Position = final_position;
    position_pass = position;
    normal_pass = normal; 
    color_pass = color;
}