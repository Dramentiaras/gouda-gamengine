#version 150 core

uniform sampler2D texture_diffuse;

in vec4 color;
in vec2 texCoord;

out vec4 out_Color;

void main(void) {
	vec4 texColor = texture2D(texture_diffuse, texCoord);
	out_Color = color * texColor;
}