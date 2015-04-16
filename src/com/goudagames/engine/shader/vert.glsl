#version 150 core

uniform mat4 model;
uniform mat4 projection;
uniform mat4 view;

in vec2 in_Position;
in vec4 in_Color;

out vec4 color;

void main() {
	gl_Position = projection * view * model * vec4(in_Position, 0, 1);
	color = in_Color;
}