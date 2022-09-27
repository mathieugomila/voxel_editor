#version 330 core
layout (location = 0) in vec3 position; 
layout (location = 1) in vec3 normal; 
layout (location = 2) in vec3 color;
layout (location = 3) in float id;
  
out vec3 color_pass; 

uniform mat4 MVP;

void main()
{
    float r = float((int(id) & 0x000000FF) >>  0);
    float g = float((int(id) & 0x0000FF00) >>  8);
    float b = float((int(id) & 0x00FF0000) >> 16);
    color_pass = vec3(r/255.0, g/255.0, b/255.0);

    vec4 final_position = MVP * vec4(position, 1.0); 
    gl_Position = final_position;
}