#version 330 core

layout(location = 0) out vec3 final_color;
  
in vec3 color_pass; 

void main()
{
    final_color = color_pass;
} 