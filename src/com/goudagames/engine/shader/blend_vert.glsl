#version 150 core

uniform mat4 model;
uniform mat4 projection;
uniform mat4 view;

in vec2 in_Position;
in vec4 in_Color;
in vec2 in_TexCoord0;
in vec2 in_TexCoord1;

out vec4 color;
out vec2 texCoord0;
out vec2 texCoord1;

void main() {
	gl_Position = projection * view * model * vec4(in_Position, 0, 1);
	color = in_Color;
	texCoord0 = in_TexCoord0;
	texCoord1 = in_TexCoord1;
}