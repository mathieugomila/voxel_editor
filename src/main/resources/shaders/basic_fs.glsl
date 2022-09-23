#version 330 core

float border_line_width = 0.04;

out vec4 final_color;
  
in vec3 position_pass;
in vec3 normal_pass; 
in vec3 color_pass; 

void main()
{
    if(abs(normal_pass.z) > 0.5 && (fract(position_pass.x) < border_line_width || fract(position_pass.y) < border_line_width || fract(position_pass.x) > 1.0 - border_line_width || fract(position_pass.y) > 1.0 - border_line_width)){
        final_color = vec4(0,0,0, 1);
    }
    else if(abs(normal_pass.x) > 0.5 && (fract(position_pass.y) < border_line_width || fract(position_pass.z) < border_line_width || fract(position_pass.y) > 1.0 - border_line_width || fract(position_pass.z) > 1.0 - border_line_width)){
        final_color = vec4(0,0,0, 1);
    }
    else if(abs(normal_pass.y) > 0.5 && (fract(position_pass.z) < border_line_width || fract(position_pass.x) < border_line_width || fract(position_pass.z) > 1.0 - border_line_width || fract(position_pass.x) > 1.0 - border_line_width)){
        final_color = vec4(0,0,0, 1);
    }
    else {

    final_color = vec4(color_pass, 1);
    }
} 